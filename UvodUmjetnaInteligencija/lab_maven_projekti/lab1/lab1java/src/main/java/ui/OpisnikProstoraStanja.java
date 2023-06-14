package ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * Razred koji predstavlja opisnik prostora stanja.
 * Cuva osnovne podatke potrebne za provedbu nekog algoritma.
 * U njemu cuvam pocetno i sva ciljna stanja.
 * Sadrzi prijelaze za svako stanje u obliku mape, kljuc je stanje, a 
 * vrijednost je lista objekata tipa Pair koji predstavlja dijete kao stanje i 
 * udaljenost do djeteta.
 * @author filip
 *
 */
public class OpisnikProstoraStanja{
	private String pocetnoStanje;
	private Set<String> ciljnoStanje;
	private Map<String, List<Pair>> prijelazi;
	private Map<String, Boolean> visited;
	
	public OpisnikProstoraStanja() {
		this.prijelazi = new HashMap<>();
	}
	
	public Map<String, Boolean> getVisited() {
		return visited;
	}

	public void setVisited(Map<String, Boolean> visited) {
		this.visited = visited;
	}

	public String getPocetnoStanje() {
		return pocetnoStanje;
	}
	public void setPocetnoStanje(String pocetnoStanje) {
		this.pocetnoStanje = pocetnoStanje;
	}
	public Set<String> getCiljnoStanje() {
		return ciljnoStanje;
	}
	public void setCiljnoStanje(Set<String> ciljnoStanje) {
		this.ciljnoStanje = ciljnoStanje;
	}
	public Map<String, List<Pair>> getPrijelazi() {
		return prijelazi;
	}
	public void setPrijelazi(Map<String, List<Pair>> prijelazi) {
		this.prijelazi = prijelazi;
	}
	
	public void dodajPrijelaz(String key, List<Pair> prijelazi) {
		this.prijelazi.put(key, prijelazi);
	}
}
