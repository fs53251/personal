package hr.fer.zemris.scripting.elems;

/**
 * Razred <code>ElementVariable</code> predstavlja element varijablu.
 * 
 * @author Filip
 *
 */
public class ElementVariable extends Element {
	/**
	 * varijabla u kojoj čuvamo naziv elementa varijable
	 */
	private String name;

	private Element value;

	/**
	 * Konstruktor
	 * 
	 * @param name naziv varijabla
	 */
	public ElementVariable(String name) {
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

	public Element getValue() {
		return value;
	}

	public void setValue(Element value) {
		this.value = value;
	}

	/**
	 * Metoda koja predstavlja ElementVariable kao string.
	 * 
	 * @return String
	 */
	@Override
	public String asText() {
		return this.name;
	}
}
