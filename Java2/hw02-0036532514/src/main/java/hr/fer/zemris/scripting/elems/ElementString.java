package hr.fer.zemris.scripting.elems;

/**
 * Razred <code>ElementString</code> predstavlja element string.
 * 
 * @author Filip
 *
 */
public class ElementString extends Element {
	/**
	 * Varijabla u kojoj čuvamo string.
	 */
	private String value;

	/**
	 * Konstruktor
	 * 
	 * @param value
	 */
	public ElementString(String value) {
		this.value = value;
	}

	/**
	 * Getter varijable value
	 * 
	 * @return String
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * Metoda koja predstavlja ElementString kao string.
	 * 
	 * @return String
	 */
	@Override
	public String asText() {
		return this.value;
	}

	/**
	 * Metoda koja predstavlja ElementString kao string, ali 
	 * u ispisu dodaje navodnike sa svake strane stringa.
	 * 
	 * @return String
	 */
	public String asText1() {
		StringBuilder sb = new StringBuilder();
		sb.append('"');
		sb.append(this.value);
		sb.append('"');
		return sb.toString();
	}
}
