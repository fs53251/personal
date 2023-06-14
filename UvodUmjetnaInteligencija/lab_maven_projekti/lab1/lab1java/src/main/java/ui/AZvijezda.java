package ui;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

public class AZvijezda {
	/**
	 * Metoda prima pocetno stanje, implementaciju funkcijskih sucelja Function, Predicate, ToDoubleFunction.
	 * funkcijaSljedbenika je "ugovor" izmedu klijenta i sucelja takav da klijent preda stanje, a 
	 * sucelje zna vratiti listu njegovih sljedbenika i udaljenost do svakog sljedbenika u obliku instance razreda Pair.
	 * ispitajCiljnoStanje je "ugovor" izmedu klijenta i sucelja takav da klijent preda stanje, a 
	 * sucelje zna vratiti true ili false, ovisno je li predano stanje ciljno stanje ili nije.
	 * funkcijaHeuristike je "ugovor" izmedu klijenta i sucelja takav da klijent preda stanje, a 
	 * sucelje zna vratiti heuristiku za dano stanje. Heuristiku vraća kao broj tipa double.
	 * 
	 * ALGORITAM:
	 * 	Dodajem pocetno stanje u listu open. Ulazi se u while petlju koja radi dokle god ima elemenata u listi open.
	 *  Skida se element iz liste open i provjerava se je li on ciljno stanje, ako je vraca se. 
	 *  Inace prolazim kroz sve sljedbenike dohvacenog cvora i prvo provjeravam je li sljedbenik vec posjecen, ako nije
	 *  izracunavam cijenu puta do dohvacenog sljedbenika i na tu cijenu dodajem cijenu heuritike.
	 *  Sad je potrebno proci po svim elementima liste open i potraziti ako se dohvaceno stanje sljedbenik nalazi u listi open.
	 *  Ako je sljedbenik u listi open, treba provjertiti je li njegova ukupna cijena(uz heuristiku) veca od cijene koju smo izracunali,
	 *  ako je onda je potrebno izbaciti element iz liste open i dodati novo izracunati, odnosno dodajemo isto to stanje u listu open, ali
	 *  samo s "jeftinijim" putem.
	 * 
	 * 
	 */
	public static Cvor aZvijezda(String pocetnoStanje, Function<String, List<Pair>> funkcijaSljedbenika, Predicate<String> ispitajCiljnoStanje, ToDoubleFunction<String> funkcijaHeuristike){
		
		Queue<Cvor> listaOpen = new PriorityQueue<Cvor>((n1, n2) -> Double.compare(n1.getUkupnaCijena(), n2.getUkupnaCijena()));
		listaOpen.add(new Cvor(pocetnoStanje, null, 0.0, new HashSet<>(), funkcijaHeuristike.applyAsDouble(pocetnoStanje)));
		Set<String> posjeceni = new HashSet<>();
		
		while(!listaOpen.isEmpty()) {
			Cvor trenutniCvor = listaOpen.remove();
			posjeceni.add(trenutniCvor.getState());
			if(ispitajCiljnoStanje.test(trenutniCvor.getState())) {
				return trenutniCvor;
			}
	
			Iterator<Pair> it1 = funkcijaSljedbenika.apply(trenutniCvor.getState()).iterator();
			
			while(it1.hasNext()) {
				Pair sljedeceStanje = it1.next();
				if(!posjeceni.contains(sljedeceStanje.getStanje())) {
					double cijena = trenutniCvor.getCost() + sljedeceStanje.getCijenaPrijelaza();
					double cijenaUzHeuristiku = cijena + funkcijaHeuristike.applyAsDouble(sljedeceStanje.getStanje());
					int zastavica = 0;
					
					Iterator<Cvor> it = listaOpen.iterator();
					while(it.hasNext()) {
						Cvor cvorIzOpen = it.next();
						if(cvorIzOpen.getState().equals(sljedeceStanje.getStanje())) {
							if(cvorIzOpen.getUkupnaCijena() > cijenaUzHeuristiku) {
								it.remove();
							}else {
								zastavica++;
							}
						}
					}
					
					if(zastavica == 0) {
						listaOpen.add(new Cvor(sljedeceStanje.getStanje(), trenutniCvor, cijena, posjeceni, cijenaUzHeuristiku));
					}
				}
			}
		}
		return null;
	}
}
