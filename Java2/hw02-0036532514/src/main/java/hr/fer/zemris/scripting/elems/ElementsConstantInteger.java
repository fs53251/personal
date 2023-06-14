package hr.fer.zemris.scripting.elems;

/**
 * Razred <code>ElementsConstantInteger</code> predstavlja element konstantnog
 * 		cijelog broja.
 * 
 * @author Filip
 *
 */

public class ElementsConstantInteger extends Element {
	/**
	 * varijabla u kojoj čuvamo vrijednost
	 */
	private int value;

	/**
	 * Konstruktor
	 * 
	 * @param value
	 */
	public ElementsConstantInteger(int value) {
		this.value = value;
	}

	/**
	 * Getter članske varijable value.
	 * 
	 * @return int
	 */
	public int getValue() {
		return this.value;
	}

	/**
	 * Metoda koja predstavlja ElementsConstantInteger kao string.
	 * 
	 * @return String
	 */
	@Override
	public String asText() {
		return Integer.toString(value);
	}
}
