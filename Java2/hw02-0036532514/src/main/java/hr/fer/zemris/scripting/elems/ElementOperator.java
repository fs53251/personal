package hr.fer.zemris.scripting.elems;

/**
 /**
 * Razred <code>ElementOperator</code> predstavlja element operator.
 * 
 * @author Filip
 *
 */
public class ElementOperator extends Element {
	/**
	 * varijabla u kojoj čuvamo element operatora
	 */
	private String symbol;

	/**
	 * Konstruktor
	 * 
	 * @param symbol string operatora 
	 */
	public ElementOperator(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * Getter članske varijable symbol.
	 * 
	 * @return String
	 */
	public String getSymbol() {
		return this.symbol;
	}

	/**
	 * Metoda koja predstavlja ElementOperator kao string.
	 * 
	 * @return String
	 */
	@Override
	public String asText() {
		return this.symbol;
	}
}
