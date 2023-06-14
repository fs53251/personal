package ui;

import java.util.Set;

/**
 * Razred koji sadrzi sve potrebne podatke koje sam koristio u svim algoritmima.
 * Za neke algoritme mi neki podaci nisu trebali, ali sam ih radi jednostavnosti 
 * sve stavio pod isti razred Cvor. 
 * @author filip
 *
 */
public class Cvor implements Comparable<Cvor> {
	
	private Cvor roditeljskiCvor;
	private String stanje;
	private double cijenaPutaDoCvora;
	private Set<String> posjeceniCvorovi;
	private double ukupnaCijena;
	
	
	/**
	 * Metoda konstruktora
	 * @param state String koji predstavlja naziv stanja
	 * @param parent referenca na objekt istog tipa, koji je roditelj ovog cvora
	 * @param cost cijena prevaljenog puta kod ovog stanja
	 * @param visited polje visited koje predstavlja polje svih posjecenih stanja u algoritmu
	 * @param ukupnaCijena ukupna cijena stanja s ukljucenom heuristikom
	 */
	public Cvor(String state, Cvor parent, double cost, Set<String> visited, double ukupnaCijena) {
		this.roditeljskiCvor = parent;
		this.stanje = state;
		this.cijenaPutaDoCvora = cost;
		this.posjeceniCvorovi = visited;
		this.ukupnaCijena = ukupnaCijena;
	}

	public double getUkupnaCijena() {
		return ukupnaCijena;
	}

	public Cvor getParent() {
		return roditeljskiCvor;
	}

	public void setParent(Cvor parent) {
		this.roditeljskiCvor = parent;
	}

	public String getState() {
		return stanje;
	}

	public void setState(String state) {
		this.stanje = state;
	}

	public double getCost() {
		return cijenaPutaDoCvora;
	}

	public void setCost(double cost) {
		this.cijenaPutaDoCvora = cost;
	}

	public Set<String> getVisited() {
		return posjeceniCvorovi;
	}

	public void setVisited(Set<String> visited) {
		this.posjeceniCvorovi = visited;
	}
	
	public int getDepth() {
		int depth = 0;
		Cvor current = this.getParent();
		while(current != null) {
			depth++;
			current = current.getParent();
		}
		return depth;
	}
	
	@Override
	public String toString() {
		return String.format(" %s ", stanje);
	}
	
	/**
	 * Metoda koja poziva roditelje dokle god ne dode do roditelja null.
	 * Koriste se rekurzivni pozivi metode da bi ispisao cijeli put.
	 * @param node
	 * @return
	 */
	public String ispisiPutProlaskomPoRoditeljima(Cvor node) {
		StringBuilder sb = new StringBuilder();
		rekurzivniPozivRoditelja(sb, node);
		return sb.toString();
	}

	private void rekurzivniPozivRoditelja(StringBuilder sb, Cvor node) {
		if(node.getParent() != null) {
			rekurzivniPozivRoditelja(sb, node.getParent());
			sb.append("=>");
		}
		sb.append(node);
	}

	@Override
	public int compareTo(Cvor other) {
		if(this.stanje instanceof String) {
			if(Double.compare(this.cijenaPutaDoCvora, other.cijenaPutaDoCvora) == 0) {
				return ((String)this.getState()).compareTo((String)other.getState());
			}
		}
		return Double.compare(this.cijenaPutaDoCvora, other.cijenaPutaDoCvora);
	}
}
