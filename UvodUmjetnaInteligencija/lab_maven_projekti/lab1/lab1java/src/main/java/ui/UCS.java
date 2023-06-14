package ui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class UCS {
	/**
	 * Metoda prima pocetno stanje, implementaciju funkcijskih sucelja Function i Predicate.
	 * funkcijaSljedbenika je "ugovor" izmedu klijenta i sucelja takav da klijent preda stanje, a 
	 * sucelje zna vratiti listu njegovih sljedbenika i udaljenost do svakog sljedbenika u obliku instance razreda Pair.
	 * ispitajCiljnoStanje je "ugovor" izmedu klijenta i sucelja takav da klijent preda stanje, a 
	 * sucelje zna vratiti true ili false, ovisno je li predano stanje ciljno stanje ili nije.
	 * 
	 * Koristim prioritetni red jer najjeftinije cvorove stavlja na pocetak reda. Da bi dobro sortiralo Cvorove potrebno je 
	 * u razredu Cvor nadjacati metodu compareTo ili implementirati sucelje Comparator i predati konstruktoru. Implementirao sam 
	 * compareTo jer cu PriorityQueue koristiti u jos nekim algoritmima pa zelim izbjeci redundanciju koda.
	 * 
	 * Koristim skup posjecenih cvorova zbog potreba ispisa laboratorijske vjezbe, ali zbog optimizacije uvodim mapu cijenaDoSadIzracunata.
	 * Kljuc te mape je stanje(String), a vrijednost ce biti Dobule koji ce predstavljati dosad najbolje izracunatu cijenu za to stanje.
	 *
	 * ALGORITAM:
	 * 	U prioritetni red dodam pocetno stanje, oznacim da je do sad najbolja izracunata cijena do njega 0.
	 *  Provodim algoritam dokle god imam elemenata u redu. Za svaki cvor ispitam je li ciljni i ako je vratim ga.
	 *  Inace ga dodam u skup posjecenih i prolazim po svim sljedbenicima. Za svakog sljedbenika provjerim koja je do sad za njega
	 *  izracunata najbolja cijena i ako je novaCijena bolja od trenutne, izmijenim novu cijenu za taj cvor u mapi cijenaDoSadIzracunata.
	 *  Dodam cvor u prioritetni red.
	 *  
	 * @param pocetnoStanje pocetnoStanje kao string
	 * @param funkcijaSljedbenika funkcijsko sucelje Function
	 * @param ispitajCiljnoStanje funkcijsko sucelje Predicate
	 * @return Cvor
	 */
	public static Cvor ucs(String pocetnoStanje, Function<String, List<Pair>> funkcijaSljedbenika, Predicate<String> ispitajCiljnoStanje){
		PriorityQueue<Cvor> priotitetniRed = new PriorityQueue<>();
		priotitetniRed.add(new Cvor(pocetnoStanje, null, 0.0, new HashSet<>(), 0.0));
		Set<String> posjeceni = new HashSet<>();
		
		Map<String, Double> cijenaDoSadIzracunata = new HashMap<>();
		cijenaDoSadIzracunata.put(pocetnoStanje, 0.);
		
		while(!priotitetniRed.isEmpty()) {
			Cvor current = priotitetniRed.poll();
			if(ispitajCiljnoStanje.test(current.getState())) {
				return current;
			}
			
			posjeceni.add(current.getState());
			
			Iterator<Pair> it = funkcijaSljedbenika.apply(current.getState()).iterator();
			while(it.hasNext()) {
				Pair sljedeceStanje = it.next();
				double novaCijena = cijenaDoSadIzracunata.get(current.getState()) + sljedeceStanje.getCijenaPrijelaza();
				double staraCijena = cijenaDoSadIzracunata.getOrDefault(sljedeceStanje.getStanje(), 0.);
				
				if(!(cijenaDoSadIzracunata.containsKey(sljedeceStanje.getStanje())) || novaCijena < staraCijena) {
					cijenaDoSadIzracunata.put(sljedeceStanje.getStanje(), novaCijena);
					priotitetniRed.add(new Cvor(sljedeceStanje.getStanje(), current, novaCijena, new HashSet<>(), 0.0));
				}
			}
		}
		
		return null; 
	}
}
