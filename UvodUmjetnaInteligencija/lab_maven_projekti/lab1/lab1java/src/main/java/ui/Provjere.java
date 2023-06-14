package ui;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
/**
 * Razred pruza algoritme provjere potrebne za laboratorijsku vjezbu. 
 * @author filip
 *
 */
public class Provjere {	
	/**
	 * Metoda pruza provjeru konzistentnosti. Za svako stanje prolazi po njegovim sljedbenicima, izdvaja heuristike za trenutno stanje i za svu 
	 * djecu po kojoj prolazi. Provjerava je li heuristika konzistentna, odnosno gleda ako razlika heuristike roditelja i heuristike djeteta ne precjenjuje
	 * odnosno, manja je ili jednaka stvarnoj udaljenosti izmedu roditelja i djeteta. 
	 * @param funkcijaSljedbenika funkcijsko sucelje koje zna vratiti sve sljedbenike za dano stanje
	 * @param funkcijaHeuristike funkcijsko sucelj koje zna vratiti heuristiku za svako stanje
	 * @param keySet skup svih stanja
	 */
	public static void checkConsitent(Function<String, List<Pair>> funkcijaSljedbenika, ToDoubleFunction<String> funkcijaHeuristike, Set<String> keySet) {
		Set<Boolean> cons = keySet.stream()
			.sorted(Comparator.naturalOrder())
			.map((state) -> {
			Double s1 = funkcijaHeuristike.applyAsDouble(state);
			Set<Boolean> s = funkcijaSljedbenika.apply(state).stream()
				.sorted(new Comparator<Pair>() {

					@Override
					public int compare(Pair o1, Pair o2) {
						return o1.getStanje().compareTo(o2.getStanje());
					}

				})
				.map((stateCostPair) -> {
				Double s2 = funkcijaHeuristike.applyAsDouble(stateCostPair.getStanje());
				Double c = stateCostPair.getCijenaPrijelaza();
				Boolean consistent = true;
				
				if(s1 - s2 <= c) {
					System.out.println(String.format(Locale.US, "[CONDITION]: %s h(%s) <= h(%s) + c: %.1f <= %.1f + %.1f", "[OK]", state, stateCostPair.getStanje(), s1, s2, c));
				}else {
					consistent = false;
					System.out.println(String.format(Locale.US, "[CONDITION]: %s h(%s) <= h(%s) + c: %.1f <= %.1f + %.1f", "[ERR]", state, stateCostPair.getStanje(), s1, s2, c));
				}
				return consistent;
			}).filter(consistent -> !consistent).collect(Collectors.toSet());
			
			if(s.contains(false)) {
				return false;
			}
			return true;
		}).filter(con -> !con).collect(Collectors.toSet());
		
		if(cons.contains(false)) {
			System.out.println("[CONCLUSION]: Heuristic is not consistent.");
		}else {
			System.out.println("[CONCLUSION]: Heuristic is consistent.");
		}
	}

	/**
	 * Funkcija koja ispituje optimisticnost. Ispituje na temelju stvarne udaljenosti odabranog stanja do 
	 * cijljnog stanja i heruristike za to stanje. Stvarnu udaljenost izracunava koristeci metodu ucs(), a
	 * heuristiku dobiva preko funkcijaHeuristike sucelja. 
	 * @param funkcijaSljedbenika
	 * @param ispitajCiljnoStanje
	 * @param funkcijaHeuristike
	 * @param keySet
	 */
	public static void checkOptimistic(Function<String, List<Pair>> funkcijaSljedbenika, Predicate<String> ispitajCiljnoStanje, ToDoubleFunction<String> funkcijaHeuristike, Set<String> keySet) {
		Set<Boolean> opt =  keySet.stream().sorted(Comparator.naturalOrder())
			.map(state -> {
				Double heuristic = funkcijaHeuristike.applyAsDouble(state);
				
				Cvor result = UCS.ucs(state, funkcijaSljedbenika, ispitajCiljnoStanje);
				Double realValue = result.getCost();
				Boolean optimistic = true;
				
				if(heuristic <= realValue) {
					System.out.println(String.format(Locale.US ,"[CONDITION]: %s h(%s) <= h*: %.1f <= %.1f", "[OK]", state, heuristic, realValue));
				}else {
					optimistic = false;
					System.out.println(String.format(Locale.US, "[CONDITION]: %s h(%s) <= h*: %.1f <= %.1f", "[ERR]", state, heuristic, realValue));
				}
				
				return optimistic;
			}).collect(Collectors.toSet());
		
		if(opt.contains(false)) {
			System.out.println("[CONCLUSION]: Heuristic is not optimistic.");
		}else {
			System.out.println("[CONCLUSION]: Heuristic is optimistic.");
		}
	}
}
