package hr.fer.zemris.scripting.elems;

/**
 * Razred <code>ElementConstantDouble</code> predstavlja element konstantnog
 * 		double broja.
 * 
 * @author Filip
 *
 */
public class ElementConstantDouble extends Element {
	/**
	 * varijabla u kojoj čuvamo vrijednost
	 */
	private double value;

	/**
	 * Konstruktor
	 * 
	 * @param value
	 */
	public ElementConstantDouble(double value) {
		this.value = value;
	}

	/**
	 * Getter članske varijable value.
	 * 
	 * @return double
	 */
	public double getValue() {
		return this.value;
	}

	/**
	 * Metoda koja predstavlja ElementConstantDouble kao string.
	 * 
	 * @return String
	 */
	@Override
	public String asText() {
		return Double.toString(value);
	}
}
