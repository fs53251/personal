package hr.fer.zemris.scripting.nodes;

import hr.fer.zemris.scripting.elems.Element;
import hr.fer.zemris.scripting.elems.ElementFunction;
import hr.fer.zemris.scripting.elems.ElementString;

/**
 * Razred <code>DocumentNode</code> predstavlja korijenski čvor stabla parsiranja.
 * 
 * @author Filip
 *
 */
public class DocumentNode extends Node {

	/**
	 * Metoda provjerava jesu li objekt nad kojim je metoda pozvana i 
	 * predani objekt jednaki. Jednakost se provjerava nad metodom isti().
	 * 
	 * @param obj objekt s kojim želimo usporediti
	 * @return boolean <code>true</code> ako su dva objekta isti prema metodi isti().
	 * 				   <code>false</code> inače.
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		if(obj instanceof DocumentNode) {
			DocumentNode other = (DocumentNode) obj;
			return isti(this, other);
		} else {
			return false;
		}
	}

	/**
	 * Metoda <code>isti</code> rekurzivno prolazi oba predana čvora.
	 * U svakom pozivu rekurzije, provjerava jesu li djeca korijenskog čvora DocumentNode
	 * izvedeni iz iste klase i provjerava imaju li isti ispis metode toString.
	 * Tako metoda ispituje jesu li predani čvorovi, ista stabla s istim tipovima djece,
	 * te čuvaju li ta djeca iste vrijednosti.
	 * 
	 * @param d1 Prvi čvor čije "stablo" uspoređujemo
	 * @param d2 Drugi čvor čije "stablo" uspoređujemo
	 * 
	 * @return <code>true</code> ako oba čvora imaju istu strukturu stabla
	 * 		   <code>false</code> inače.
	 */
	public boolean isti(Node d1, Node d2) {
		int djeca1 = d1.numberOfChildren();
		int djeca2 = d2.numberOfChildren();
		Node dijetePrvog, dijeteDrugog;

		if(djeca1 == djeca2 && djeca1 > 0) {
			boolean provjera = true;
			for(int i = 0; i < djeca1; i++) {
				dijetePrvog = d1.getChild(i);
				dijeteDrugog = d2.getChild(i);
				provjera = isti(dijetePrvog, dijeteDrugog);
			}
			return provjera;
		} else {
			return (d1.getClass().equals(d2.getClass()) && d1.toString().equals(d2.toString()));
		}
	}

	/**
	 * Metoda koja obilazi cijelo stablo parsiranja i vraća ulazni text preko stringa.
	 * Rezultat metode oslanja se na metodu ispis().
	 * 
	 * @return String ispis ulaza na temelju "stabla" spremljenog u korijenskom čvoru 
	 * 				  DocumentNode
	 */
	@Override
	public String toString() {
		return ispis(this);
	}

	/**
	 *Medota prima čvor Node. Ako je čvor instanca razreda koji može imati djecu, poziva se rekurzija i 
	 *obilaze se sva djeca, neovisno o dubini stabla. Za čvorove koji nemaju djecu, ispisuje se 
	 *vrijednost koju čuvaju.
	 * 
	 * @param n referenca na čvor
	 * @return String rekonstruirani ulazni tekst na temelju stabla parsiranja
	 */
	public String ispis(Node n) {
		if(n instanceof TextNode) {
			return ((TextNode) n).getText();
		} else if(n instanceof EchoNode) {
			Element[] lista = ((EchoNode) n).getElements();
			StringBuilder sb = new StringBuilder();
			sb.append("{$= ");
			for(int i = 0; i < lista.length; i++) {
				if(lista[i] instanceof ElementString) {
					sb.append('\"' + lista[i].asText() + "\" ");
				} else if(lista[i] instanceof ElementFunction) {
					sb.append('@' + lista[i].asText() + " ");
				} else {
					sb.append(lista[i].asText() + " ");
				}

			}
			sb.append("$}");
			return sb.toString();
		} else if(n instanceof ForLoopNode) {
			int broj = n.numberOfChildren();
			StringBuilder sb = new StringBuilder();

			ForLoopNode fln = (ForLoopNode) n;
			sb.append("{$ FOR ");
			sb.append(fln.getVariable().asText() + " ");
			sb.append(fln.getStartExpression().asText() + " ");
			sb.append(fln.getEndExpression().asText() + " ");
			if(fln.getStepExpression() != null)
				sb.append(fln.getStepExpression().asText());
			sb.append(" $}");
			for(int i = 0; i < broj; i++) {
				sb.append(ispis(n.getChild(i)));
			}
			sb.append("{$ END $}");
			return sb.toString();
		}

		int broj = n.numberOfChildren();
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < broj; i++) {
			sb.append(ispis(n.getChild(i)));
		}
		return sb.toString();
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitDocumentNode(this);
	}
}
