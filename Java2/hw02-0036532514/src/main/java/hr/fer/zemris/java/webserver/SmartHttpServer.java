package hr.fer.zemris.java.webserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hr.fer.zemris.java.webserver.RequestContext.RCCookie;
import hr.fer.zemris.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.scripting.parser.SmartScriptParser;

public class SmartHttpServer {
	private String address;
	private String domainName;
	private int port;
	private int workerThreads;
	private int sessionTimeout;
	private Map<String, String> mimeTypes = new HashMap<String, String>();
	private ServerThread serverThread;
	private ExecutorService threadPool;
	private Path documentRoot;

	private boolean startedServer = false;
	private final String workersPackage = "hr.fer.zemris.java.webserver.workers.";
	//private Map<String, IWebWorker> workersMap = new HashMap<>();

	private Map<String, SessionMapEntry> sessions = new HashMap<>();
	private Random sessionRandom = new Random();

	public SmartHttpServer(String configFileName) {
		FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(configFileName);
			Properties properties = new Properties();
			properties.load(inputStream);

			address = properties.getProperty("server.address");
			domainName = properties.getProperty("server.domainName");
			port = Integer.parseInt(properties.getProperty("server.port"));
			workerThreads = Integer.parseInt(properties.getProperty("server.workerThreads"));
			documentRoot = Paths.get(properties.getProperty("server.documentRoot"));
			String mimeConfig = properties.getProperty("server.mimeConfig");
			sessionTimeout = Integer.parseInt(properties.getProperty("session.timeout"));
			String workersConfig = properties.getProperty("server.workers");

			//load mime properties
			FileInputStream is = new FileInputStream(mimeConfig);
			Properties mimeProperties = new Properties();
			mimeProperties.load(is);
			mimeTypes.put("html", mimeProperties.getProperty("html"));
			mimeTypes.put("htm", mimeProperties.getProperty("htm"));
			mimeTypes.put("txt", mimeProperties.getProperty("txt"));
			mimeTypes.put("gif", mimeProperties.getProperty("gif"));
			mimeTypes.put("png", mimeProperties.getProperty("png"));
			mimeTypes.put("jpg", mimeProperties.getProperty("jpg"));

			//load worker properties
//			try(BufferedReader br = new BufferedReader(new FileReader(workersConfig))) {
//				String l;
//				while((l = br.readLine()) != null) {
//					l = l.trim();
//					if(l.startsWith("#") || l.isEmpty()) {
//						continue;
//					}
//
//					if(l.split("=").length < 2) {
//						throw new IllegalArgumentException();
//					}
//					String path = l.split("=")[0].trim();
//					String FQCN = l.split("=")[1].trim();
//
//					if(workersMap.containsKey(path)) {
//						throw new IllegalArgumentException();
//					} else {
//						try {
//							Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(FQCN);
//							Object newObject = referenceToClass.getDeclaredConstructor().newInstance();
//							IWebWorker iww = (IWebWorker) newObject;
//
//							workersMap.put(path, iww);
//						} catch(Exception e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			}

		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	protected synchronized void start() {
		if(!startedServer) {
			threadPool = Executors.newFixedThreadPool(workerThreads);
			serverThread = new ServerThread();
			serverThread.start();
			startedServer = true;
		}

		Thread clean = new Thread(() -> {
			while(true) {
				try {
					Thread.sleep(5 * 60 * 1000);
				} catch(InterruptedException e) {}
				sessions.values().removeIf(SessionMapEntry::isExpired);
			}
		});
		clean.setDaemon(true);
		clean.start();
	}

	protected synchronized void stop() {
		if(startedServer) {
			serverThread.interrupt();
			startedServer = false;
			threadPool.shutdown();
		}
	}

	protected class ServerThread extends Thread {
		@SuppressWarnings("resource")
		@Override
		public void run() {
			try {
				ServerSocket serverSocket = new ServerSocket();
				serverSocket.bind(new InetSocketAddress(InetAddress.getByName(address), port));

				while(true) {
					Socket client = serverSocket.accept();
					ClientWorker cw = new ClientWorker(client);
					threadPool.submit(cw);
				}
			} catch(IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}

	private class ClientWorker implements Runnable, IDispatcher {
		private Socket csocket;
		private InputStream istream;
		private OutputStream ostream;
		private String version;
		private String method;
		private String host;
		private Map<String, String> params = new HashMap<String, String>();
		private Map<String, String> tempParams = new HashMap<String, String>();
		private Map<String, String> permPrams = new HashMap<String, String>();
		private List<RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();
		private String SID;

		private RequestContext context = null;

		public ClientWorker(Socket csocket) {
			super();
			this.csocket = csocket;
		}

		@Override
		public void run() {
			try {
				istream = new BufferedInputStream(csocket.getInputStream());
				ostream = new BufferedOutputStream(csocket.getOutputStream());

				List<String> request = readRequest();

				if(request.size() < 1) {
					sendEmptyResponse(ostream, 400, "Bad Request");
					return;
				}

				String getHostFromHeader = getHost(request);
				if(getHostFromHeader == null) {
					host = domainName;
				} else {
					host = getHostFromHeader;
				}

				checkSession(request);

				String[] firstLine = request.get(0).split(" ");
				if(firstLine == null || firstLine.length != 3) {
					sendEmptyResponse(ostream, 400, "Bad Request");
					return;
				}

				method = firstLine[0].toUpperCase().trim();
				if(!method.equals("GET")) {
					sendEmptyResponse(ostream, 400, "Bad Request");
					return;
				}

				version = firstLine[2].toUpperCase().trim();
				if(!version.equals("HTTP/1.0") && !version.equals("HTTP/1.1")) {
					sendEmptyResponse(ostream, 400, "Bad Request");
					return;
				}

				String requestedPath = firstLine[1].trim();
				String path = null;
				String paramString = null;

				if(requestedPath.contains("?")) {
					path = requestedPath.split("\\?")[0];
					paramString = requestedPath.split("\\?")[1];
				} else {
					path = requestedPath;
				}
				path = path.substring(1);

				if(paramString != null) {
					parseParameters(paramString);
				}
				internalDispatchRequest(path, true);

			} catch(IOException e) {
				e.printStackTrace();
			} finally {
				if(ostream != null) {
					try {
						ostream.flush();
					} catch(IOException e) {}
				}
				try {
					csocket.close();
				} catch(IOException e) {
					System.out.println("Error: " + e.getMessage());
				}
			}

		}

		private synchronized void checkSession(List<String> request) {
			String sidCandidate = null;
			for(String line : request) {
				if(line.startsWith("Cookie:")) {
					line = line.substring(7);

					String[] cookies = line.trim().split(";");
					for(String cookie : cookies) {
						String[] pair = cookie.split("=");
						if(pair[0].trim().toLowerCase().equals("sid")) {
							sidCandidate = pair[1].trim().substring(1);
							sidCandidate = sidCandidate.substring(0, sidCandidate.lastIndexOf('"'));
						}
					}
				}
			}

			if(sidCandidate == null) {
				dodajCookie();
			} else {
				if(sessions.containsKey(sidCandidate)) {
					SessionMapEntry sessionMapEntry = sessions.get(sidCandidate);
					if(!host.equals(sessionMapEntry.getHost())) {
						dodajCookie();
						return;
					}

					if(System.currentTimeMillis() / 1000 > sessionMapEntry.getValidUntil()) {
						sessions.remove(sidCandidate);
						dodajCookie();
						return;
					}

					sessionMapEntry.setValidUntil(System.currentTimeMillis() / 1000 + sessionTimeout);
					permPrams = sessionMapEntry.getMap();
				} else {
					dodajCookie();
				}

			}
		}

		private void dodajCookie() {
			String sid = generateSessionId();
			SessionMapEntry sme = new SessionMapEntry();
			sme.setSid(sid);
			sme.setValidUntil(System.currentTimeMillis() / 1000 + sessionTimeout);
			sme.setMap(permPrams);
			sme.setHost(host);

			sessions.put(sid, sme);

			if(context == null) {
				context = new RequestContext(ostream, params, permPrams, outputCookies, tempParams, this, sid);
				RCCookie cooki = new RCCookie("sid", sid, null, host, "/");
				cooki.setHttpOnly(true);
				context.addRCCookie(cooki);
			}
		}

		private String generateSessionId() {
			final int sessionLen = 20;
			final String alph = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < sessionLen; i++) {
				int index = sessionRandom.nextInt(alph.length());
				sb.append(alph.charAt(index));
			}
			return sb.toString();
		}

		private void parseParameters(String paramString) {
			Arrays.stream(paramString.split("&")).forEach(pair -> {
				String name = pair.split("=")[0];
				String value = pair.split("=")[1];
				params.put(name, value);
			});
		}

		private String getHost(List<String> lines) {
			for(String line : lines) {
				if(line.trim().startsWith("Host:")) {
					String[] response = line.trim().split(":");
					if(response.length > 1) {
						return response[1].trim();
					} else {
						return null;
					}
				}
			}

			return null;
		}

		private List<String> readRequest() throws IOException {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buf = new byte[2048];
			int read;
			while((read = istream.read(buf)) != -1) {
				bos.write(buf, 0, read);
				if(bos.toString(StandardCharsets.US_ASCII).contains("\r\n\r\n")) {
					break;
				}
			}
			String requestStr = new String(bos.toByteArray(), StandardCharsets.US_ASCII);
			return extractHeaders(requestStr);
		}

		//split string by lines, take care of multiline attributes 9(horizontal tab), 31(space)
		private List<String> extractHeaders(String requestHeader) {
			List<String> headers = new ArrayList<String>();
			String currentLine = null;
			for(String s : requestHeader.split("\n")) {
				if(s.isEmpty())
					break;
				char c = s.charAt(0);
				if(c == 9 || c == 32) {
					currentLine += s;
				} else {
					if(currentLine != null) {
						headers.add(currentLine);
					}
					currentLine = s;
				}
			}
			if(!currentLine.isEmpty()) {
				headers.add(currentLine);
			}
			return headers;
		}

		@Override
		public void dispatchRequest(String urlPath) throws Exception {
			internalDispatchRequest(urlPath, false);
		}

		private void internalDispatchRequest(String path, boolean directCall) throws IOException {

//			if(workersMap.containsKey("/" + path)) {
//				try {
//					if(context == null) {
//						context = new RequestContext(ostream, params, permPrams, outputCookies, tempParams, this);
//					}
//					workersMap.get("/" + path).processRequest(context);
//				} catch(Exception e) {
//					e.printStackTrace();
//				}
//				return;
//			}

			if(path.startsWith("private")) {
				if(directCall) {
					sendEmptyResponse(ostream, 404, "Not Found");
					return;
				}
			}

			if(path.startsWith("mi/vrijeme")) {
				try {
					Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(workersPackage + "VrijemeWorker");
					Object newObject = referenceToClass.getDeclaredConstructor().newInstance();
					IWebWorker iww = (IWebWorker) newObject;

					if(context == null) {
						context = new RequestContext(ostream, params, permPrams, outputCookies, tempParams, this, SID);
					}
					iww.processRequest(context);
				} catch(Exception e) {
					sendEmptyResponse(ostream, 404, "Not Found");
					return;
				}
				return;
			}

			if(path.startsWith("mi/choose")) {
				try {
					Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(workersPackage + "PozadinaWorker");
					Object newObject = referenceToClass.getDeclaredConstructor().newInstance();
					IWebWorker iww = (IWebWorker) newObject;

					if(context == null) {
						context = new RequestContext(ostream, params, permPrams, outputCookies, tempParams, this, SID);
					}
					iww.processRequest(context);
				} catch(Exception e) {
					sendEmptyResponse(ostream, 404, "Not Found");
					return;
				}
				return;
			}

			if(path.startsWith("calc")) {
				try {
					Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(workersPackage + "SumWorker");
					Object newObject = referenceToClass.getDeclaredConstructor().newInstance();
					IWebWorker iww = (IWebWorker) newObject;

					if(context == null) {
						context = new RequestContext(ostream, params, permPrams, outputCookies, tempParams, this, SID);
					}
					iww.processRequest(context);
				} catch(Exception e) {
					sendEmptyResponse(ostream, 404, "Not Found");
					return;
				}
				return;
			}

			if(path.startsWith("index2.html")) {
				try {
					Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(workersPackage + "Home");
					Object newObject = referenceToClass.getDeclaredConstructor().newInstance();
					IWebWorker iww = (IWebWorker) newObject;

					if(context == null) {
						context = new RequestContext(ostream, params, permPrams, outputCookies, tempParams, this, SID);
					}
					iww.processRequest(context);
				} catch(Exception e) {
					sendEmptyResponse(ostream, 404, "Not Found");
					return;
				}
				return;
			}

			if(path.startsWith("setbgcolor")) {
				try {
					Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(workersPackage + "BgColorWorker");
					Object newObject = referenceToClass.getDeclaredConstructor().newInstance();
					IWebWorker iww = (IWebWorker) newObject;

					if(context == null) {
						context = new RequestContext(ostream, params, permPrams, outputCookies, tempParams, this, SID);
					}
					iww.processRequest(context);
				} catch(Exception e) {
					sendEmptyResponse(ostream, 404, "Not Found");
					return;
				}
				return;
			}

			if(path.startsWith("ext/")) {
				String className = path.substring(4);
				try {
					Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(workersPackage + className);
					Object newObject = referenceToClass.getDeclaredConstructor().newInstance();
					IWebWorker iww = (IWebWorker) newObject;

					if(context == null) {
						context = new RequestContext(ostream, params, permPrams, outputCookies, tempParams, this, SID);
					}
					iww.processRequest(context);
				} catch(Exception e) {
					sendEmptyResponse(ostream, 404, "Not Found");
					return;
				}
				return;
			}

			if(path.startsWith("mi/images")) {
				path = path.substring(3);
			}

			Path reqPath = documentRoot.resolve(path);
			if(!reqPath.startsWith(documentRoot)) {
				sendEmptyResponse(ostream, 403, "Forbidden");
				return;
			}

			File reqFile = reqPath.toFile();
			if(!reqFile.exists() || !reqFile.isFile() || !reqFile.canRead()) {
				sendEmptyResponse(ostream, 404, "Not Found");
				return;
			} else {
				String filename = reqFile.getName();
				if(filename.contains(".") && !filename.endsWith(".")) {
					String extension = filename.substring(filename.lastIndexOf(".") + 1);

					if(extension.equals("smscr")) {
						String documentBody = null;
						try {
							documentBody = Files.readString(reqPath);
						} catch(IOException e) {
							e.printStackTrace();
						}

						if(context == null) {
							context = new RequestContext(ostream, params, permPrams, outputCookies, tempParams, this, SID);
						}
						new SmartScriptEngine(
								new SmartScriptParser(documentBody).getDocumentNode(),
								context).execute();
					} else {

						String mimeType = mimeTypes.getOrDefault(extension, "application/octet-stream");

						if(context == null) {
							context = new RequestContext(ostream, params, permPrams, outputCookies, tempParams, this, SID);
						}

						context.setMimeType(mimeType);

						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						InputStream is = Files.newInputStream(reqPath);
						byte[] buf = new byte[1024];
						int read;
						while((read = is.read(buf)) != -1) {
							bos.write(buf, 0, read);
						}
						byte[] file = bos.toByteArray();

						context.setContentLength((long) file.length);

						context.write(file);
					}
				}
			}
		}
	}

	private static void sendResponseWithData(OutputStream cos, int statusCode, String statusText, String contentType, byte[] data) throws IOException {

		cos.write(
				("HTTP/1.1 " + statusCode + " " + statusText + "\r\n" + "Server: simple java server\r\n" + "Content-Type: " + contentType + "\r\n" + "Content-Length: " + data.length + "\r\n" + "Connection: close\r\n" + "\r\n")
						.getBytes(StandardCharsets.US_ASCII));
		cos.write(data);
		cos.flush();
	}

	private static void sendEmptyResponse(OutputStream cos, int statusCode, String statusText) throws IOException {
		sendResponseWithData(cos, statusCode, statusText, "text/plain;charset=UTF-8", new byte[0]);
	}

	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Expected only one argument!!!");
			return;
		}

		String configFileName = args[0];
		SmartHttpServer smartHTTPServer = new SmartHttpServer(configFileName);
		smartHTTPServer.start();
	}

	private static class SessionMapEntry {
		String sid;
		String host;
		long validUntil;
		Map<String, String> map;

		public boolean isExpired() {
			return System.currentTimeMillis() > validUntil;
		}

		public String getSid() {
			return sid;
		}

		public void setSid(String sid) {
			this.sid = sid;
		}

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public long getValidUntil() {
			return validUntil;
		}

		public void setValidUntil(long validUntil) {
			this.validUntil = validUntil;
		}

		public Map<String, String> getMap() {
			return map;
		}

		public void setMap(Map<String, String> map) {
			this.map = map;
		}

	}
}
