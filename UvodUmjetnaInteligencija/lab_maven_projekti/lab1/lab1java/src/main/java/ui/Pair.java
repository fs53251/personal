package ui;

/**
 * Razred Pair sluzi za cuvanje prijelaza i njihovih vrijednosti.
 * Ako neki cvor "s0" ima sljebenika "s1" i do njega ima put od 10, 
 * razred Pair cuva vrijednosti (stanje = s1, cijenaPrijelaza = 10) za cvor "s0".
 * @author filip
 *
 */
public class Pair{
	
	private String stanje;
	private Double cijenaPrijelaza;
	
	/**
	 * Konstruktor, prima parametrizirano stanje S i cijenu prijelaza kao double.
	 * @param state stanje
	 * @param cost cijena prijelaza
	 */
	public Pair(String stanje, double cijena) {
		super();
		this.stanje = stanje;
		this.cijenaPrijelaza = cijena;
	}

	/**
	 * Getter stanja
	 * @return stanje
	 */
	public String getStanje() {
		return stanje;
	}
	
	/**
	 * Getter cijene prijelaza
	 * @return cijena prijelaza
	 */
	public double getCijenaPrijelaza() {
		return cijenaPrijelaza;
	}
}
