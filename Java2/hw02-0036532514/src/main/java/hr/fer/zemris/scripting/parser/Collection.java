package hr.fer.zemris.scripting.parser;

/**
 * Sučelje <code>Collection</code> korisnicima daje osnovni paket apstraktnih 
 * metoda koje neki razred treba nadjačati.
 * 
 * @author Filip
 *
 */
public interface Collection {

	/**
	 * Metoda provjerava je li kolekcija prazna.
	 * Ponuđena je defaultna implementacija.
	 * 
	 * @return <code>true</code> ako je kolekcija prazna;
	 * 		   <code>false</code> inače.
	 */
	default boolean isEmpty() {
		return this.size() == 0 ? true : false;
	}

	/**
	 * Aprstraktna metoda, njenim nadjačavanjem će se vratiti broj 
	 * elemenata kolekcije.
	 * @return
	 */
	int size();

	/**
	 * Apstraktna metoda, njenim nadjačavanjem će se 
	 * dodavati objekti u kolekciju.
	 * 
	 * @param value objekt koji treba dodati u kolekciju.
	 */
	void add(Object value);

	/**
	 * Apstraktna metoda, njenim nadjačavanjem će se provjeravati
	 *  sadrži li kolekcija traženi objekt.
	 * 
	 * @param value objekt za koji prvjeravamo nalazi li se u kolekciji
	 * @return
	 */
	boolean contains(Object value);

	/**
	 * Apstraktna metoda, njenim nadjačavanjem će se 
	 * vaditi objekt, predan kao argument, iz kolekcije.
	 * 
	 * @param value objekt koji treba izbaciti.
	 * @return
	 */
	boolean remove(Object value);

	/**
	 * Apstraktna metoda, njenim nadjačavanjem će se 
	 * vraćati reprezentacija kolekcije kao polja.
	 * 
	 * @return
	 */
	Object[] toArray();

	/**
	 * Metoda koja prima referencu na kolekciju i 
	 * sve njene elemente dodaje u trenutnu kolekciju.
	 * Ponuđena je defaultna implementacija.
	 * 
	 * @param other referenca na kolekciju čije elemente treba dodati
	 */
	default void addAll(Collection other) {
		/**
		 * * Razred <code>AddToCollectionProcessor</code> nasljeđuje Processor.
		 *
		 * @author Filip
		 *
		 */
		class AddToCollectionProcessor implements Processor {
			/**
			 * Metoda dodaje objekt, predan kao argument, u kolekciju.
			 */
			public void process(Object value) {
				Collection.this.add(value);
			}
		}
		other.forEach(new AddToCollectionProcessor());
	}

	/**
	 * Apstraktna metoda, njenim nadjačavanjem će se brisati elementi kolekcije.
	 */
	void clear();

	/**
	 * Apstraktna metoda, njenim nadjačavanjem će 
	 * vraćati kapacitet kolekcije.
	 * @return
	 */
	int capacity();

	/**
	 * Apstraktna metoda.
	 * Stvaramo ElementsGetter nad kolekcijom.
	 * 
	 * @return ElementsGetter
	 */
	ElementsGetter createElementsGetter();

	/**
	 * Default metoda.
	 * Prima kolekciju i referencu na razred Tester. U kolekciju nad kojom je pozvana,
	 * metoda dodaje elemente primljene kolekcije, ali samo one koji zadovoljavaju uvjet
	 * metode test().
	 * 
	 * @param col referenca na kolekciju 
	 * @param tester referenca razreda Tester, koristi se njena metoda test za ispitivanje
	 * 				 elemenata koje treba dodati.
	 */
	default void addAllSatisfying(Collection col, Tester tester) {
		ElementsGetter eg = col.createElementsGetter();
		while(eg.hasNextElement()) {
			Object o = eg.getNextElement();
			if(tester.test(o))
				this.add(o);
		}
	}

	/**
	 * Apstraktna metoda, njenim nadjačavanjem će prolaziti po svim
	 *  objektima kolekcije i nad njima pozivati metodu process().
	 * 
	 * @param processor referenca na instancu klasse Processor
	 */
	default void forEach(Processor processor) {
		ElementsGetter eg = this.createElementsGetter();
		while(eg.hasNextElement()) {
			processor.process(eg.getNextElement());
		}
	}
}
