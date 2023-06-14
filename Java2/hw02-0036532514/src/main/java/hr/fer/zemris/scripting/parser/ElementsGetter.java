package hr.fer.zemris.scripting.parser;

/**
 * Sučelje <code>ElementsGetter</code> korisnicima daje osnovni paket apstraktnih 
 * metoda koje neki razred treba nadjačati.
 * 
 * @author Filip
 *
 */
public interface ElementsGetter {
	/**
	 * Apstraktna metoda.
	 * Ispituje ima li kolekcija sljedeći član.
	 * 
	 * @return boolean <code>true</code> ako kolekcija ima sljedeći član.
	 * 				   <code>false</code> inače.
	 */
	boolean hasNextElement();

	/**
	 * Apstraktna metoda.
	 * Vraća sljedeći element kolekcije.
	 * 
	 * @return Object sljedeći element kolekcije.
	 */
	Object getNextElement();

	/**
	 * Default metoda koja prima kao argument referencu na Processor i koristi njezinu
	 * metodu process nad svim elementima kolekcije.
	 * 
	 * @param p referenca na instancu klase Processor
	 * @return void
	 */
	default void processRemaining(Processor p) {
		while(this.hasNextElement()) {
			p.process(this.getNextElement());
		}
	}
}
