package hr.fer.zemris.scripting.parser;

/**
 * Razred <code>ObjectStack</code> korisnicima daje
 * implementaciju stoga. U pozadini stog implementira 
 * poljem objekata.
 * 
 * @author Filip
 *
 */
public class ObjectStack {
	private ArrayIndexedCollection ac = new ArrayIndexedCollection();

	/**
	 * Metoda provjerava je li stog prazan.
	 * 
	 * @return <code>true</code> ako je stog prazan;
	 * 		   <code>false</code> inače.
	 */
	public boolean isEmpty() {
		return ac.isEmpty();
	}

	/**
	 * Metoda vraća broj elemenata na stogu.
	 * 
	 * @return size broj elemenata stoga
	 */
	public int size() {
		return ac.size();
	}

	/**
	 * Metoda koja stavlja element na vrh stoga.
	 * 
	 * @param value objekt koji se dodaje na stog.
	 */
	public void push(Object value) {
		ac.add(value);
	}

	/**
	 * Metoda uzima i briše objekt s vraha stoga.
	 * 
	 * @return Objekt objekt koji je skinut s vrha stoga
	 * @throws EmptyStackException ako je pozvana metoda, 
	 * 			a nema elemenata na stogu.
	 */
	public Object pop() {
		if(this.isEmpty())
			throw new EmptyStackException();
		Object catchElement = ac.get(ac.size() - 1);
		ac.remove(ac.size() - 1);
		return catchElement;
	}

	/**
	 * Metoda vraća element s vrha stoga i ne briše ga.
	 * 
	 * @return Object objekt koji se nalazi na vrhu stoga
	 * @throws EmptyStackException
	 */
	public Object peek() {
		if(this.isEmpty())
			throw new EmptyStackException();
		return ac.get(ac.size() - 1);
	}

	/**
	 * Metoda koja briše elemente stoga.
	 */
	public void clear() {
		ac.clear();
	}
}
