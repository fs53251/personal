package ui;

import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class BFS {
	/**
	 * Metoda prima pocetno stanje, implementaciju funkcijskih sucelja Function i Predicate.
	 * funkcijaSljedbenika je "ugovor" izmedu klijenta i sucelja takav da klijent preda stanje, a 
	 * sucelje zna vratiti listu njegovih sljedbenika i udaljenost do svakog sljedbenika u obliku instance razreda Pair.
	 * ispitajCiljnoStanje je "ugovor" izmedu klijenta i sucelja takav da klijent preda stanje, a 
	 * sucelje zna vratiti true ili false, ovisno je li predano stanje ciljno stanje ili nije.
	 * 
	 * Algoritam zavrsava pronalaskom ciljnog stanja i vracam objekt tipa Cvor koji nosi poruku o stanju, roditelju tog stanja, 
	 * cijeni puta do tog stanja, skupu posjecenih cvorova i heuristiku koja je za slucaj bfs algoritma uvijek 0.0 jer ovjde mi taj 
	 * podatak ne treba i ne igra ulogu, koristim istu strukturu za A* algoritam stoga je ovo polje unutar konstruktora razreda Cvor -> NEEFIKASNO!
	 * 
	 * ALGORITAM:
	 * 	Dok ima elemenata u listi open, skinem element s pocetka liste open. Oznacim ga kao posjecenog i ispitam je li upravo to stanje ciljno, ako vratim ga.
	 *  Ako nije ciljno stanje, treba proci po svim njegovim sljedbenicima koje mi zna vratiti funkcijaSljedbenika. Svako dijete ako vec nije posjeceno,
	 *  dodam u litu open.
	 * 
	 * @param pocetnoStanje pocetno stanje
	 * @param funkcijaSljedbenika implementacija funkcijskog sucelja Function
	 * @param ispitajCljnoStanje implementacija funkcijsog sucelja Predicate
	 * @return Cvor
	 */
	public static Cvor bfs(String pocetnoStanje, Function<String, List<Pair>> funkcijaSljedbenika, Predicate<String> ispitajCljnoStanje){
		
		if(ispitajCljnoStanje.test(pocetnoStanje)) {
			return new Cvor(pocetnoStanje, null, 0.0, new HashSet<>(), 0.0);
		}
		
		Deque<Cvor> listaOpen = new LinkedList<>();
		Set<String> posjeceni = new HashSet<>();
	
		listaOpen.add(new Cvor(pocetnoStanje, null, 0.0, new HashSet<>(), 0.0));
		
		while(!listaOpen.isEmpty()) {
			Cvor trenutnoStanje = listaOpen.removeFirst();
			posjeceni.add(trenutnoStanje.getState());
			if(ispitajCljnoStanje.test(trenutnoStanje.getState())) {
				return trenutnoStanje;
			}
			
			Iterator<Pair> it = funkcijaSljedbenika.apply(trenutnoStanje.getState()).iterator();
			Pair sljedeceStanje;
			while(it.hasNext()) {
				sljedeceStanje = it.next();
				if(!posjeceni.contains(sljedeceStanje.getStanje())) {
					listaOpen.addLast(new Cvor(sljedeceStanje.getStanje(), trenutnoStanje, trenutnoStanje.getCost() + sljedeceStanje.getCijenaPrijelaza(), posjeceni, 0.0));
				}
				
			}
		}
		return null;
	}
}
