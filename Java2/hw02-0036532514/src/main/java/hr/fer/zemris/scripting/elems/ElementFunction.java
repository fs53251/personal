package hr.fer.zemris.scripting.elems;

/**
 * Razred <code>ElementFunction</code> predstavlja element funkcije.
 * 
 * @author Filip
 *
 */
public class ElementFunction extends Element {
	/**
	 * Varijabla koja čuva ime funkcije
	 */
	private String name;

	/**
	 * Konstruktor
	 * 
	 * @param name naziv funkcije
	 */
	public ElementFunction(String name) {
		this.name = name;
	}

	/**
	 * Getter članske varijable name.
	 * 
	 * @return String
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Metoda koja predstavlja ElementFunction kao string.
	 * 
	 * @return String
	 */
	@Override
	public String asText() {
		return this.name;
	}

	/**
	 * Metoda koja predstavlja ElementFunction kao string.
	 * Dodaje znak <code>@</code> ispred imena funkcije.
	 * 
	 * @return String
	 */
	public String asText1() {
		StringBuilder sb = new StringBuilder();
		sb.append("@");
		sb.append(this.name);
		return sb.toString();
	}

}
