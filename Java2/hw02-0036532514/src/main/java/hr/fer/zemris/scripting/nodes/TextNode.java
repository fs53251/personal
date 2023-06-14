package hr.fer.zemris.scripting.nodes;

/**
 * Razred <code>TextNode</code> je čvor koji predstavlja dio teksta.
 * 
 * @author Filip
 *
 */
public class TextNode extends Node {
	private String text;

	/**
	 * Konstruktor.
	 * 
	 * @param text
	 */
	public TextNode(String text) {
		this.text = text;
	}

	/**
	 * Metoda za ispis. Vraća tekst koji drži u sebi.
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		return "TextNode [text=" + text + "]";
	}

	/**
	 * Getter texta
	 * @return String vraća tekst čvora
	 */
	public String getText() {
		return this.text;
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitTextNode(this);
	}

}
