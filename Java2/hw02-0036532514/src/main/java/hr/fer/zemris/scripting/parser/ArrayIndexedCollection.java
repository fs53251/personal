package hr.fer.zemris.scripting.parser;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * Razred <code>ArrayIndexedCollection</code> korisnicima daje
 * promjenjivu kolekciju objekata.
 * 
 * <p>Inicijalno je alocirano polje 16 elemenata. Kod promjene veličine polja,
 * ono se udvostruči. Različitim konstruktorima možemo alocirati različite veličine polja</p>
 * 
 * @author Filip
 *
 */
public class ArrayIndexedCollection implements List {
	private int size;
	private Object[] elements;
	private long modificationCount = 0L;

	/**
	 * Metoda konstruktor, kao argument prima Kolekciju i inicijalni kapacitet.
	 * Alocira polje na veličinu predanog kapaciteta i 
	 * popunjava ga elementima predane kolekcije u argumentu.
	 * Inicijalni kapacitet mora biti bilo koji prirodni broj.
	 * Predana kolekcija ne smije biti <code>null</code>.
	 * 
	 * @param other referenca na kolekciju za čije elemente želimo alocirati polje
	 * @param initialCapacity kapacitet polja
	 * @throws IllegalArgumentException ako je za kapacitet predan cijeli broj manji od 1.
	 * @throws NullPointerException ako je predana kolekcija u argumentu <code>null</code>.
	 */
	public ArrayIndexedCollection(Collection other, int initialCapacity) {
		if(other == null) {
			throw new NullPointerException();
		}
		if(initialCapacity < other.size()) {
			this.elements = new Object[other.size()];
		} else {
			this.elements = new Object[initialCapacity];
		}
		this.size = 0;
		this.addAll(other);

	}

	/**
	 * Metoda konstruktor, prima kolekciju i delegira zadaću 
	 * konstrukcije objekata.
	 *
	 * @param other predana referenca na kolekciju
	 */
	public ArrayIndexedCollection(Collection other) {
		this(other, 16);
	}

	/**
	 * Metoda konstruktor, prima inicijalni kapacitet i delegira zadaću 
	 * konstukcije objekata.
	 * 
	 * @param initialCapacity cijeli broj predan kao početni kapacitet alociranog polja
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		if(initialCapacity < 1) {
			throw new IllegalArgumentException();
		}
		this.size = 0;
		this.elements = new Object[initialCapacity];
	}

	/**
	 * Default konstruktor, delegira zadaću konstrukcije objekata.
	 */
	public ArrayIndexedCollection() {
		this(16);
	}

	/**
	 * Metoda vraća broj dodanih elemenata u dvostruko povezanu listu.
	 * 
	 * @return <code>int</code> broj elemenata
	 */
	@Override
	public int size() {
		return this.size;
	}

	@Override
	/**
	 * Metoda dodaje Objekt, predan kao argument, na prvo slobodno mjesto polja.
	 * Složenost dodavanja elementa je O(1), osim ako treba realocirati polje zbog popunjenosti. 
	 * Argument ne smije biti <code>null</code>.
	 * 
	 * @param value objekt koji je potrebno dodati
	 * @return <code>void</code>
	 * @throws NullPointerException ako se kao argument preda 
	 * 		   vrijednost <code>null</code>
	 */
	//O(1) if there is free space for Object value
	//O(n) if we are reallocation array on heap
	public void add(Object value) {
		if(value == null)
			throw new NullPointerException();
		if(this.size == elements.length) {
			Object[] temp = new Object[this.size * 2];
			for(int i = 0; i < this.size; i++) {
				temp[i] = this.elements[i];
			}
			this.elements = temp;
		}
		this.elements[this.size] = value;
		this.size++;
		this.modificationCount++;
	}

	/**
	 * Metoda traži Objekt, predan kao arguemnt, u polju.
	 * Složenost traženja elementa je O(n).
	 * 
	 * @param value objekt koji tražimo u polju
	 * @return <code>true</code> ako je predani argument u polju;
	 * 		   <code>false</code> ako se element ne nalazi u polju. 
	 */
	@Override
	public boolean contains(Object value) {
		for(int i = 0; i < this.size; i++) {
			if(this.elements[i].equals(value)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Metoda vraća polje objekata ove kolekcije.
	 * 
	 * @return Object[] polje objekata kolekcije.
	 */
	@Override
	public Object[] toArray() {
		return this.elements;
	}

	/**
	 * Metoda sve elemente polja postavlja na null.
	 * Ne mijenja se kapacitet polja.
	 * Složenost metode je O(n).
	 * 
	 */
	@Override
	public void clear() {
		for(int i = 0; i < this.size; i++) {
			this.elements[i] = null;
		}
		this.size = 0;
	}

	/**
	 * Metoda vadi Objekt, predan kao argument, iz polja. Za ispitivanje 
	 * jednakosti objekata se oslanja na metodu equals.
	 * Složenost micanja objekta van polja je O(n). Argument ne smije 
	 * biti <code>null</code>
	 * 
	 * @param value objekt koji treba izbaciti iz polja
	 * @return <code>true</code> ako je predani argument izbačen iz polja;
	 * 		   <code>false</code> ako je kao ovjekt predan null ili nije pronađen
	 * 		   element u polju.
	 */
	@Override
	public boolean remove(Object value) {
		if(value == null)
			return false;
		int index = -1;
		boolean removed = false;
		for(int i = 0; i < this.size(); i++) {
			if(this.elements[i].equals(value)) {
				index = i;
				removed = true;
				break;
			}
		}
		if(removed) {
			for(int i = index; i < this.size - 1; i++) {
				this.elements[i] = this.elements[i + 1];
			}
			this.elements[this.size - 1] = null;
		}
		this.size--;
		this.modificationCount++;
		return removed;
	}

	/**
	 * Metoda dohvaća objekt polja na poziciji index.
	 * Argument može biti bilo koji cijeli broj koji 
	 * odgovara poziciji nekog	objekta u polju, inače 
	 * javlja iznimku.
	 * Složenost metode je O(1).
	 * 
	 * @param index broj pozicije elementa u polju
	 * @return Object traženi objekt na poziciji index
	 * @throws IndexOutOfBoundsException ako predani index nije između 
	 * 			0 i indexa zadnjeg elementa polja.
	 */
	public Object get(int index) {
		if(index < 0 || index >= this.size) {
			throw new IndexOutOfBoundsException();
		}
		return this.elements[index];
	}

	/**
	 * Metoda ubacuje objekt polje,	na poziciju predanu kao argument.
	 * Pozicija ne smije biti van granica 0 i broja elemenata u polju.
	 * Objekt koji predajemo kao argument ne smije biti <code>null</code>.
	 * Složenost dodavanja objekta je O(n);
	 * 
	 * @param value predani objekt koji treba dodati u polje
	 * @param position pozicija na koju želimo ubaciti objekt,
	 * 		  objekt koji se nalazio do sada na toj poziciji, 
	 * 		  poprima index position+1.
	 * @throws NullPointerException ako je predan <code>null</code> kao objekt.
	 * @throws IndexOutOfBoundsException ako nije predana pozicija unutar granica elemenata polja.
	 */
	public void insert(Object value, int position) {
		if(position < 0 || position > size)
			throw new IndexOutOfBoundsException();
		if(value == null)
			throw new NullPointerException();

		if(this.size == elements.length) {
			Object[] temp = new Object[this.size * 2];
			for(int i = 0; i < this.size; i++) {
				temp[i] = this.elements[i];
			}
			this.elements = temp;
		}
		for(int i = this.size; i > position; i--) {
			this.elements[i] = this.elements[i - 1];
		}
		this.elements[position] = value;
		this.size++;
		this.modificationCount++;
	}

	/**
	 * Metoda vraća poziciju objekta, predanog kao argument, u polju.
	 * Složenost metode je O(n). Argument može biti bilo koji objekt.
	 * 
	 * @param value objekt za koji metoda traži poziciju u polju
	 * @return pozicija predanog objekta u polju
	 */
	public int indexOf(Object value) {
		if(value == null)
			return -1;
		int povratniIndex = -1;
		for(int i = 0; i < this.size; i++) {
			if(this.elements[i].equals(value)) {
				povratniIndex = i;
				break;
			}
		}
		return povratniIndex;
	}

	/**
	 * Metoda vadi element polja, na poziciji koja je predana kao argument.
	 * Argument je bilo koji cijeli broj koji predstavlja poziciju elementa u polju kojeg 
	 * treba ukloniti.
	 * 
	 * @param index predana pozicija
	 * @throws IndexOutOfBoundsException ako predana pozicija nije unutar 
	 * 		   granica 0 i pozicije zadnjeg elementa polja.
	 */
	public void remove(int index) {
		if(index < 0 || index >= this.size)
			throw new IndexOutOfBoundsException();
		for(int i = index; i < this.size - 1; i++) {
			this.elements[i] = this.elements[i + 1];
		}
		this.elements[this.size - 1] = null;
		this.size--;
		this.modificationCount++;
		return;
	}

	/**
	 * Metoda koja vraća kapacitet polja.
	 * 
	 * @return size broj elemenata polja.
	 */
	public int capacity() {
		return this.elements.length;
	}

	/**
	 * Metoda vraća referencu na instancu razreda koji implementira sučelje ElementsGetter.
	 * 
	 * @return ElementsGetter
	 */
	@Override
	public ElementsGetter createElementsGetter() {
		return new EG(this);
	}

	/**
	 * Privatni statički razred <code>EG</code> predstavlja implementaciju 
	 * ElementsGettera koji zna dohvaćati i vraćati elemente.
	 * 
	 * @author Filip
	 *
	 */
	private static class EG implements ElementsGetter {
		private ArrayIndexedCollection ar;

		/**
		 * Varijabla koja čuva index elementa na kojem smo trenutno.
		 */
		private int nextIndex = 0;

		/**
		 * Varijabla koja pamti broj modifikacija kako bi znali je li kolekcija strukturno mijenjana
		 */
		private long savedModificationCount;

		/**
		 * Konstruktor
		 * 
		 * @param ar referenca na polje elemenata čije elemente želi dohvaćati.
		 */
		public EG(ArrayIndexedCollection ar) {
			this.ar = ar;
			this.savedModificationCount = ar.modificationCount;
		}

		/**
		 * Metoda koja ispituje, postoji li sljedeći element u kolekciji.
		 * 
		 * @return boolean <code>true</code> ako postoji sljedeći element.
		 * 				   <code>false</code> inače.
		 */
		@Override
		public boolean hasNextElement() {
			return ar.elements[nextIndex] != null ? true : false;
		}

		/**
		 * Metoda vraća element kolekcije na koji pokazuje varijabla nextIndex.
		 * Provjerava se je li se dogodila modifikacija nakon stvaranja instance razreda 
		 * 
		 * @return Object element kolekcije.
		 * @throws ConcurrentModificationException ako je modificirano polje, nakon stvaranja instance 
		 * 											razreda EG.
		 */
		@Override
		public Object getNextElement() {
			if(this.savedModificationCount != ar.modificationCount)
				throw new ConcurrentModificationException();

			if((Object) ar.elements[nextIndex] == null) {
				throw new NoSuchElementException();
			}
			return (Object) ar.elements[nextIndex++];
		}
	}
}
