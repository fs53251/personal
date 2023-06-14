package hr.fer.zemris.scripting.parser;

/**
 * Razred <code>SmartScriptingParserException</code> predstavlja iznimku kod parsiranja
 * ulaznog niza.
 * 
 * @author Filip
 *
 */
public class SmartScriptingParserException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default konstruktor.
	 */
	public SmartScriptingParserException() {
		super();
	}

	/**
	 * Konstruktor koji prima poruku.
	 * @param poruka
	 */
	public SmartScriptingParserException(String poruka) {
		super(poruka);
	}

}
