package hr.fer.zemris.scripting.nodes;

import java.util.Arrays;

import hr.fer.zemris.scripting.elems.Element;

/**
 * Razred <code>EchoNode</code> predstavlja čvor koji dinamički čuva neki dio unosa.
 * 
 * @author Filip
 *
 */
public class EchoNode extends Node {
	/**
	 * Polje objekata tipa Element, koji predstavljaju sve objekte čvora EchoNode.
	 */
	private Element[] elements;

	/**
	 * Metoda getter, vraća referencu na polje objekata.
	 * @return Element[]
	 */
	public Element[] getElements() {
		return elements;
	}

	/**
	 * Konstruktor
	 * @param elements
	 */
	public EchoNode(Element[] elements) {
		super();
		this.elements = elements;
	}

	/**
	 * Nadjačana metoda za ispis
	 * 
	 * @return String ispis sadrži sve elemente iz polja elements
	 */
	@Override
	public String toString() {
		return "EchoNode [elements=" + Arrays.toString(elements) + "]";
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitEchoNode(this);
	}

}
