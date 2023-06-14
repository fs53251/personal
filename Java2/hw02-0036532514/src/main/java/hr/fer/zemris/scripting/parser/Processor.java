package hr.fer.zemris.scripting.parser;

/**
 * Sučelje <code>Proccesor</code>.
 * Ima apstraktnu metodu process.
 * 
 * @author Filip
 *
 */
public interface Processor {
	/**
	 * Apstraktna metoda process
	 * @param value objekt koji prima kao argument
	 */
	void process(Object value);
}
