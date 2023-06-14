package hr.fer.zemris.scripting.parser;

/**
 * Sučelje <code>List</code> koje nasljeđuje sučelje Collection.
 * Sadrži osnovne apstraktne metode za rad s objektima tipa Lista.
 * 
 * @author Filip
 *
 */
public interface List extends Collection {
	/**
	 * Apstraktna metoda.
	 * Vraća objekt na poziciji index.
	 * 
	 * @param index pozicija na kojoj želimo dobiti element kolekcije.
	 * @return Objekt objekt na željenoj poziciji
	 */
	Object get(int index);

	/**
	 * Apstraktna metoda.
	 * Na određenu poziciju stavlja predani Objekt.
	 * 
	 * @param value Objekt koji ubacujemo u kolekciju.
	 * @param position mjesto na koje ubacujemo objek
	 */
	void insert(Object value, int position);

	/**
	 * Apstraktna metoda.
	 * Vraća poziciju prve pojave predanog objekta.
	 * 
	 * @param value Objekt čiju poziciju tražimo
	 * @return int broj pozicije
	 */
	int indexOf(Object value);

	/**
	 * Apstraktna metoda.
	 * Uklanja Objekt kolekcije na poziciji index.
	 * 
	 * @param index pozicija elemnta kojeg želimo ukloniti.
	 */
	void remove(int index);
}
