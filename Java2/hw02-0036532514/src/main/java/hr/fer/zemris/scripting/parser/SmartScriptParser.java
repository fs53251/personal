package hr.fer.zemris.scripting.parser;

import hr.fer.zemris.scripting.elems.Element;
import hr.fer.zemris.scripting.elems.ElementVariable;
import hr.fer.zemris.scripting.lexer.Lexer1;
import hr.fer.zemris.scripting.lexer.Token1;
import hr.fer.zemris.scripting.lexer.TokenType1;
import hr.fer.zemris.scripting.nodes.DocumentNode;
import hr.fer.zemris.scripting.nodes.EchoNode;
import hr.fer.zemris.scripting.nodes.ForLoopNode;
import hr.fer.zemris.scripting.nodes.Node;
import hr.fer.zemris.scripting.nodes.TextNode;

public class SmartScriptParser {
	/**
	 * referenca na lexer
	 */
	Lexer1 l;

	/**
	 * Stog kao pomoć u parsiranju
	 */
	ObjectStack stog = new ObjectStack();

	/**
	 * Varijabla tokena kojeg dohvatimo
	 */
	Token1 dohvaceniToken;

	/**
	 * Varijabla tipa dohvaćenog tokena
	 */
	TokenType1 tip = TokenType1.EOF;

	/**
	 * Varijabla vrijednosti dohvaćenog tokena
	 */
	String value;

	/**
	 * Konstruktor, prima ulaz, a zadaću parsiranja delegira u metodu parsing().
	 * Stvara instancu lexera koji će služiti za izdvajanje tokena.
	 * 
	 * @param documentBody ulazni tekst
	 */
	public SmartScriptParser(String documentBody) {
		this.l = new Lexer1(documentBody);
		parsing();
	}

	/**
	 * Metoda koja parsira zadani ulazni niz.
	 * Koristi lexer za dohvaćanje tokena, a zatim ispituje njegov tip i gradi stablo.
	 * 
	 * @throws SmartScriptingParserException ako dođe do greške u parsiranju
	 */
	public void parsing() {
		stog.push(new DocumentNode());

		do {
			dohvatiSljedeciToken();
			Node popped;
			if(tip == TokenType1.EOF) {
				break;
			} else {
				popped = (Node) stog.pop();
			}

			//TextNode
			if(tip == TokenType1.STRING && !(value.equals("$}"))) {
				TextNode tnode = new TextNode((value).toString());
				popped.addChildNode(tnode);
				stog.push(popped);

			} else if(tip == TokenType1.TAG) { //tag
				//tag "FOR"
				if(value.equals("FOR")) {
					ElementVariable ev;
					Element startEx, endEx, step;

					dohvatiSljedeciToken();
					//1st variable 
					if(tip == TokenType1.VARIJABLA) {
						ev = new ElementVariable(value);
					} else {
						throw new SmartScriptingParserException();
					}

					//element, varible, string
					ArrayIndexedCollection ar = new ArrayIndexedCollection();
					while(!(tip == TokenType1.STRING && value.equals("$}"))) {
						dohvatiSljedeciToken();
						if(!value.equals("$}"))
							ar.add((Element) dohvaceniToken.getValue());
					}

					if(ar.size() == 2) {
						startEx = (Element) ar.get(0);
						endEx = (Element) ar.get(1);
						step = null;
					} else if(ar.size() == 3) {
						startEx = (Element) ar.get(0);
						endEx = (Element) ar.get(1);
						step = (Element) ar.get(2);
					} else {
						throw new SmartScriptingParserException();
					}

					ForLoopNode fln = new ForLoopNode(ev, startEx, endEx, step);
					popped.addChildNode(fln);
					stog.push(popped);
					stog.push(fln);
				} else if(value.equals("=")) { //tag =
					ArrayIndexedCollection ar = new ArrayIndexedCollection();
					while(!(tip == TokenType1.STRING && value.equals("$}"))) {
						dohvatiSljedeciToken();
						if(!value.equals("$}"))
							ar.add((Element) dohvaceniToken.getValue());
					}
					Element[] listaElemenata = new Element[ar.size()];
					for(int i = 0; i < ar.size(); i++) {
						listaElemenata[i] = (Element) ar.get(i);
					}
					popped.addChildNode(new EchoNode(listaElemenata));
					stog.push(popped);
				} else { //end
					if(stog.isEmpty())
						throw new SmartScriptingParserException();
					dohvatiSljedeciToken();
					if(!value.equals("$}"))
						throw new SmartScriptingParserException();
				}
			}
		}
		while(tip != TokenType1.EOF);
	}

	/**
	 * Vraća referencu na objekt na stogu. Metodu korisim nakon parsiranja, stoga očekujem
	 * da se na stogu nalazi objekt tipa DocumentNode
	 * @return
	 */
	public DocumentNode getDocumentNode() {
		return (DocumentNode) this.stog.peek();
	}

	/**
	 * Pomoćna metoda parsera za dohvaćanje sljedećeg tokena i određivanje njegovog
	 * tipa i vrijednosti.
	 */
	public void dohvatiSljedeciToken() {
		dohvaceniToken = l.nextToken();
		tip = dohvaceniToken.getType();
		if(tip == TokenType1.EOF)
			return;
		value = ((Element) dohvaceniToken.getValue()).asText();
	}

}
