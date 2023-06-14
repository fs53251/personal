package hr.fer.zemris.scripting.lexer;

/**
 * Razred <code>Token1</code> predstavlja leksicku jedinku.
 * 
 * @author Filip
 *
 */
public class Token1 {
	/**
	 * Varijabla koja čuva tip tokena.
	 */
	TokenType1 type;

	/**
	 * Varijabla koja čuva vrijednost tokena.
	 */
	Object value;

	/**
	 * Konstruktor
	 * 
	 * @param type tip tokena
	 * @param value vrijednost tokena
	 */
	public Token1(TokenType1 type, Object value) {
		super();
		this.type = type;
		this.value = value;
	}

	/**
	 * Metoda getter privatne članske varijable value.
	 * 
	 * @return value vrijednost tokena
	 */
	public TokenType1 getType() {
		return type;
	}

	/**
	 * Metoda getter privatne članske varijable type.
	 * 
	 * @return type tip tokena
	 */
	public Object getValue() {
		return value;
	}

}
