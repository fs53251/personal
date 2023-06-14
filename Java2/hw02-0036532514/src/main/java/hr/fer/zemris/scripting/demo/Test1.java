package hr.fer.zemris.scripting.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;
import hr.fer.zemris.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.scripting.parser.SmartScriptParser;

public class Test1 {
	//prvi
//	public static void main(String[] args) {
//		String documentBody = null;
//		try {
//			documentBody = Files.readString(Paths.get("src/main/resources/osnovni.smscr"));
//		} catch(IOException e) {
//			e.printStackTrace();
//		}
//		Map<String, String> parameters = new HashMap<String, String>();
//		Map<String, String> persistentParameters = new HashMap<String, String>();
//		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
//		// create engine and execute it
//		new SmartScriptEngine(
//				new SmartScriptParser(documentBody).getDocumentNode(),
//				new RequestContext(System.out, parameters, persistentParameters, cookies)).execute();
//	}

	//drugi
//	public static void main(String[] args) {
//		String documentBody = null;
//		try {
//			documentBody = Files.readString(Paths.get("src/main/resources/zbrajanje.smscr"));
//		} catch(IOException e) {
//			e.printStackTrace();
//		}
//		Map<String, String> parameters = new HashMap<String, String>();
//		Map<String, String> persistentParameters = new HashMap<String, String>();
//		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
//		parameters.put("a", "4");
//		parameters.put("b", "2");
//		// create engine and execute it
//		new SmartScriptEngine(
//				new SmartScriptParser(documentBody).getDocumentNode(),
//				new RequestContext(System.out, parameters, persistentParameters, cookies)).execute();
//	}

	//treci
//	public static void main(String[] args) {
//		String documentBody = null;
//		try {
//			documentBody = Files.readString(Paths.get("src/main/resources/brojPoziva.smscr"));
//		} catch(IOException e) {
//			e.printStackTrace();
//		}
//		Map<String, String> parameters = new HashMap<String, String>();
//		Map<String, String> persistentParameters = new HashMap<String, String>();
//		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
//		persistentParameters.put("brojPoziva", "3");
//		RequestContext rc = new RequestContext(System.out, parameters, persistentParameters,
//				cookies);
//		new SmartScriptEngine(
//				new SmartScriptParser(documentBody).getDocumentNode(), rc).execute();
//		System.out.println("Vrijednost u mapi: " + rc.getPersistentParameter("brojPoziva"));
//	}

	//cetvrti
	public static void main(String[] args) {
		String documentBody = null;
		try {
			documentBody = Files.readString(Paths.get("src/main/resources/fibonaccih.smscr"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		// create engine and execute it
		new SmartScriptEngine(
				new SmartScriptParser(documentBody).getDocumentNode(),
				new RequestContext(System.out, parameters, persistentParameters, cookies)).execute();
	}
}
