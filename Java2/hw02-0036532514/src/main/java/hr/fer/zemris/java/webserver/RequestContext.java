package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RequestContext {
	private OutputStream outputStream;
	private Charset charset;

	public String encoding;
	public int statusCode;
	public String statusText;
	public String mimeType;
	public Long contentLength;

	private Map<String, String> parameters;
	private Map<String, String> temporaryParameters;
	private Map<String, String> persistentParameters;
	private List<RCCookie> outputCookies;
	private boolean headerGenerated;
	private IDispatcher dispatcher;
	private String sid;

	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies,
			Map<String, String> temporaryparameters, IDispatcher dispatcher, String sid) {
		if(outputStream == null) {
			throw new NullPointerException();
		}

		this.outputStream = outputStream;
		this.parameters = parameters == null ? new HashMap<String, String>() : parameters;
		this.persistentParameters = persistentParameters == null ? new HashMap<String, String>() : persistentParameters;
		this.outputCookies = outputCookies == null ? new ArrayList<RCCookie>() : outputCookies;
		this.temporaryParameters = temporaryparameters == null ? new HashMap<>() : temporaryparameters;
		this.dispatcher = dispatcher;

		//default values
		this.encoding = "UTF-8";
		this.statusCode = 200;
		this.statusText = "OK";
		this.mimeType = "text/html";
		this.contentLength = null;
		this.headerGenerated = false;
		this.sid = sid;
	}

	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies) {
		this(outputStream, parameters, persistentParameters, outputCookies, null, null, null);
	}

	public IDispatcher getDispatcher() {
		return dispatcher;
	}

	public String getSid() {
		return sid;
	}

	public String getParameter(String name) {
		return this.parameters.getOrDefault(name, null);
	}

	public Set<String> getParameterNames() {
		return Collections.unmodifiableSet(this.parameters.keySet());
	}

	public String getPersistentParameter(String name) {
		return this.persistentParameters.getOrDefault(name, null);
	}

	public Set<String> getPersistentParameterNames() {
		return Collections.unmodifiableSet(this.persistentParameters.keySet());
	}

	public void setPersistentParameter(String name, String value) {
		this.persistentParameters.put(name, value);
	}

	public void removePersistentParameter(String name) {
		this.persistentParameters.remove(name);
	}

	public String getTemporaryParameter(String name) {
		return this.temporaryParameters.getOrDefault(name, null);
	}

	public Set<String> getTemporaryParameterNames() {
		return Collections.unmodifiableSet(this.temporaryParameters.keySet());
	}

	public void addRCCookie(RCCookie cookie) {
		if(headerGenerated) {
			throw new RuntimeException();
		}
		this.outputCookies.add(cookie);
	}

	public void removeRCCookie(RCCookie cookie) {
		if(headerGenerated) {
			throw new RuntimeException();
		}
		this.outputCookies.remove(cookie);
	}

	public String getSessionID() {
		return "";
	}

	public void setTemporaryParameters(String name, String value) {
		this.temporaryParameters.put(name, value);
	}

	public void removeTemporaryParameter(String name) {
		this.temporaryParameters.remove(name);
	}

	public void setEncoding(String encoding) {
		if(headerGenerated) {
			throw new RuntimeException();
		}
		this.encoding = encoding;
	}

	public void setStatusCode(int statusCode) {
		if(headerGenerated) {
			throw new RuntimeException();
		}
		this.statusCode = statusCode;
	}

	public void setStatusText(String statusText) {
		if(headerGenerated) {
			throw new RuntimeException();
		}
		this.statusText = statusText;
	}

	public void setMimeType(String mimeType) {
		if(headerGenerated) {
			throw new RuntimeException();
		}
		this.mimeType = mimeType;
	}

	public void setContentLength(Long contentLength) {
		if(headerGenerated) {
			throw new RuntimeException();
		}
		this.contentLength = contentLength;
	}

	public RequestContext write(byte[] data) throws IOException {
		if(!headerGenerated) {
			generateHeader();
			headerGenerated = true;
		}
		return write(data, 0, data.length);
	}

	public RequestContext write(byte[] data, int offset, int len) throws IOException {
		if(!headerGenerated) {
			generateHeader();
			headerGenerated = true;
		}

		outputStream.write(data, offset, len);
		return this;
	}

	public RequestContext write(String text) throws IOException {
		if(!headerGenerated) {
			generateHeader();
			headerGenerated = true;
		}

		byte[] data = text.getBytes(charset);
		return write(data, 0, data.length);
	}

	private void generateHeader() {
		charset = Charset.forName(encoding);

		String contentType = mimeType.startsWith("text/") ? mimeType + ";charset=" + encoding : mimeType;

		StringBuilder sb = new StringBuilder();
		sb.append("HTTP/1.1 " + statusCode + " " + statusText + "\r\n" + "Content-Type: " + contentType + "\r\n");
		if(contentLength != null) {
			sb.append("Content-Length: " + contentLength);
		}

		outputCookies.stream().map(cookie -> {
			String cookieOutput = "Set-Cookie: ";
			cookieOutput += cookie.getName() + "=\"" + cookie.getValue() + "\"";

			if(cookie.getDomain() != null) {
				cookieOutput += "; Domain=" + cookie.getDomain();
			}

			if(cookie.getPath() != null) {
				cookieOutput += "; Path=" + cookie.getPath();
			}

			if(cookie.getMaxAge() != null) {
				cookieOutput += "; Max-Age=" + cookie.getMaxAge();
			}

			if(cookie.httpOnly) {
				cookieOutput += "; HttpOnly";
			}

			return cookieOutput;
		}).forEach(cookieLine -> sb.append(cookieLine + "\r\n"));

		sb.append("\r\n\r\n");
		byte[] header = sb.toString().getBytes(StandardCharsets.ISO_8859_1);

		try {
			outputStream.write(header);
		} catch(IOException e) {
			System.out.println("Error while writing header: ");
			e.printStackTrace();
		}
	}

	public static class RCCookie {
		private String name;
		private String value;
		private String domain;
		private String path;
		private Integer maxAge;
		private boolean httpOnly = false;

		public RCCookie(String name, String value, Integer maxAge, String domain, String path) {
			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
		}

		public boolean isHttpOnly() {
			return httpOnly;
		}

		public void setHttpOnly(boolean httpOnly) {
			this.httpOnly = httpOnly;
		}

		public String getName() {
			return name;
		}

		public String getValue() {
			return value;
		}

		public String getDomain() {
			return domain;
		}

		public String getPath() {
			return path;
		}

		public Integer getMaxAge() {
			return maxAge;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((domain == null) ? 0 : domain.hashCode());
			result = prime * result + ((maxAge == null) ? 0 : maxAge.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((path == null) ? 0 : path.hashCode());
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if(this == obj)
				return true;
			if(obj == null)
				return false;
			if(getClass() != obj.getClass())
				return false;
			RCCookie other = (RCCookie) obj;
			if(domain == null) {
				if(other.domain != null)
					return false;
			} else if(!domain.equals(other.domain))
				return false;
			if(maxAge == null) {
				if(other.maxAge != null)
					return false;
			} else if(!maxAge.equals(other.maxAge))
				return false;
			if(name == null) {
				if(other.name != null)
					return false;
			} else if(!name.equals(other.name))
				return false;
			if(path == null) {
				if(other.path != null)
					return false;
			} else if(!path.equals(other.path))
				return false;
			if(value == null) {
				if(other.value != null)
					return false;
			} else if(!value.equals(other.value))
				return false;
			return true;
		}

	}
}
