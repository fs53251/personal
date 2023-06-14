package hr.fer.zemris.scripting.nodes;

import hr.fer.zemris.scripting.parser.ArrayIndexedCollection;

/**
 * Razred <code>Node</code> predstavlja osnovni tip čvora iz kojeg 
 * ćemo graditi "stablo" parsiranja.
 * 
 * @author Filip
 *
 */
public abstract class Node {
	ArrayIndexedCollection ar = null;

	public abstract void accept(INodeVisitor visitor);

	/**
	 * Metoda dodaje čvor u interno polje.
	 * 
	 * @param child čvor koji se dodaje
	 */
	public void addChildNode(Node child) {
		if(ar == null)
			ar = new ArrayIndexedCollection();
		ar.add(child);
	}

	/**
	 * Metoda vraća broj djece čvorova.
	 * 
	 * @return int broj "djece"
	 */
	public int numberOfChildren() {
		if(ar == null)
			return 0;
		return ar.size();
	}

	/**
	 * Metoda koja služi za ispis. Potrebno ju je nadjačati
	 * jer ovdje vraća prazan string.
	 * 
	 * @return String prazan string
	 */
	@Override
	public String toString() {
		return "";
	}

	/**
	 * Metoda vraća čvor na poziciji, predanoj u argumentu, u internom polju.
	 * 
	 * @param index pozicija na kojem tražimo čvor
	 * @return Node čvor koji treba vratiti
	 */
	public Node getChild(int index) {
		return (Node) ar.get(index);
	}
}
