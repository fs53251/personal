package hr.fer.zemris.scripting.parser;

/**
 * Sučelje <code>Tester</code> sadrži metodu test koju treba nadjačati.
 * Služi za ispitivanje uvjeta nad primljenim objektom.
 * 
 * @author Filip
 *
 */
public interface Tester {
	/**
	 * 
	 * @param obj objekt nad kojim ispitujemo uvjet
	 * @return boolean <code>true</code> ako je uvjet ispravan, 
	 * 				   <code>false</code> inače.
	 */
	boolean test(Object obj);
}
