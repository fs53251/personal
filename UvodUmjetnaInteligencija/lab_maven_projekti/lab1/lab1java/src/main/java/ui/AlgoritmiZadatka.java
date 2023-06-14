package ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AlgoritmiZadatka {

	/**
	 * Metoda pripreme implmenetacije potrebnih funkcijskih sucelja.
	 * Nakon pripreme, poziva algoritam bfs i ispisuje rezultat.
	 * @param path
	 */
	public void BFS(String path) {
		try {
			OpisnikProstoraStanja prostorStanja = loadOpisnikProstoraStanje(path);
			
			Function<String, List<Pair>> funkcijaSljedbenika = (key) -> {
				return prostorStanja.getPrijelazi().get(key);
			};
			
			Predicate<String> ispitajCiljnaStanja = (key) ->{
				return prostorStanja.getCiljnoStanje().contains(key);
			};
			
			Cvor result = BFS.bfs(prostorStanja.getPocetnoStanje(), funkcijaSljedbenika, ispitajCiljnaStanja);
			
			if(result == null) {
				System.out.println("[FOUND_SOLUTION]: no");
			}else {
				System.out.println("# BFS");
				System.out.println("[FOUND_SOLUTION]: yes");
				System.out.println("[STATES_VISITED]: " + result.getVisited().size());
				System.out.println("[PATH_LENGTH]: " + result.ispisiPutProlaskomPoRoditeljima(result).split("=>").length);
				System.out.println("[TOTAL_COST]: " + result.getCost());
				System.out.println("[PATH]:" + result.ispisiPutProlaskomPoRoditeljima(result));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metoda pripreme implmenetacije potrebnih funkcijskih sucelja.
	 * Nakon pripreme, poziva algoritam ucs i ispisuje rezultat.
	 * @param path
	 */
	public void UCS(String path) {
		try {
			OpisnikProstoraStanja prostorStanja = loadOpisnikProstoraStanje(path);
			
			Function<String, List<Pair>> succ = (key) -> {
				return prostorStanja.getPrijelazi().get(key);
			};
			
			Predicate<String> predicate = (key) ->{
				return prostorStanja.getCiljnoStanje().contains(key);
			};
			
			
			Cvor result = UCS.ucs(prostorStanja.getPocetnoStanje(), succ, predicate);

			
			if(result == null) {
				System.out.println("[FOUND_SOLUTION]: no");
			}else {
				System.out.println("# UCS");
				System.out.println("[FOUND_SOLUTION]: yes");
				System.out.println("[STATES_VISITED]: " + result.getVisited().size());
				System.out.println("[PATH_LENGTH]: " + result.ispisiPutProlaskomPoRoditeljima(result).split("=>").length);
				System.out.println("[TOTAL_COST]: " + result.getCost());
				System.out.println("[PATH]:" + result.ispisiPutProlaskomPoRoditeljima(result));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metoda pripreme implmenetacije potrebnih funkcijskih sucelja.
	 * Nakon pripreme, poziva algoritam aZvijezda i ispisuje rezultat.
	 * @param path
	 */
	public void ASTAR(String path, String h) {
		try {
			OpisnikProstoraStanja prostorStanja = loadOpisnikProstoraStanje(path);
			Map<String, Double> heuristika = loadHeuristic(h);
			
			Function<String, List<Pair>> succ = (key) -> {
				return prostorStanja.getPrijelazi().get(key);
			};
			
			Predicate<String> predicate = (key) ->{
				return prostorStanja.getCiljnoStanje().contains(key);
			};
			
			ToDoubleFunction<String> doubleFunction = (key) -> {
				return heuristika.get(key);
			};
			
			Cvor result = AZvijezda.aZvijezda(prostorStanja.getPocetnoStanje(), succ, predicate, doubleFunction);
			
			if(result == null) {
				System.out.println("[FOUND_SOLUTION]: no");
			}else {
				System.out.println("# A-STAR " + h);
				System.out.println("[FOUND_SOLUTION]: yes");
				System.out.println("[STATES_VISITED]: " + result.getVisited().size());
				System.out.println("[PATH_LENGTH]: " + result.ispisiPutProlaskomPoRoditeljima(result).split("=>").length);
				System.out.println("[TOTAL_COST]: " + result.getCost());
				System.out.println("[PATH]:" + result.ispisiPutProlaskomPoRoditeljima(result));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metoda pripreme implmenetacije potrebnih funkcijskih sucelja.
	 * Nakon pripreme, poziva algoritam checkConsistent i ispisuje rezultat.
	 * @param path
	 */
	public void checkConsistent(String path, String h) {
		try {
			OpisnikProstoraStanja prostorStanja = loadOpisnikProstoraStanje(path);
			Map<String, Double> heuristika = loadHeuristic(h);
			
			Function<String, List<Pair>> succ = (key) -> {
				return prostorStanja.getPrijelazi().get(key);
			};
			
			ToDoubleFunction<String> doubleFunction = (key) -> {
				return heuristika.get(key);
			};
			
			System.out.println("# HEURISTIC-CONSISTENT " + h);
			Provjere.checkConsitent(succ, doubleFunction, heuristika.keySet());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metoda pripreme implmenetacije potrebnih funkcijskih sucelja.
	 * Nakon pripreme, poziva algoritam checkOptimistic i ispisuje rezultat.
	 * @param path
	 */
	public void checkOptimistic(String path, String h) {
		try {
			OpisnikProstoraStanja prostorStanja = loadOpisnikProstoraStanje(path);
			Map<String, Double> heuristika = loadHeuristic(h);
			
			Function<String, List<Pair>> succ = (key) -> {
				return prostorStanja.getPrijelazi().get(key);
			};
			
			ToDoubleFunction<String> doubleFunction = (key) -> {
				return heuristika.get(key);
			};
			
			Predicate<String> predicate = (key) ->{
				return prostorStanja.getCiljnoStanje().contains(key);
			};
			
			
			System.out.println("# HEURISTIC-OPTIMISTIC " + h);
			Provjere.checkOptimistic(succ, predicate, doubleFunction, heuristika.keySet());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metoda koja prima string koji predstavlja datoteku.
	 * Cita i parsira liniju po liniju datoteke.
	 * @param h
	 * @return
	 * @throws IOException
	 */
	private Map<String, Double> loadHeuristic(String h) throws IOException {
		Map<String, Double> result = new HashMap<>();
		
		InputStream is = new FileInputStream(new File(h));
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
		String line = br.readLine();
		do {
			if(line == null) {
				break;
			}
			
			String[] pair = line.split(":");
			result.put(pair[0].trim(), Double.parseDouble(pair[1].trim()));
			
			line = br.readLine();
		}while(line != null);
		
		return result;
	}

	
	/**
	 * Metoda koja parsira liniju po liniju datoteke.
	 * @param path
	 * @return
	 * @throws IOException
	 */
	private OpisnikProstoraStanja loadOpisnikProstoraStanje(String path) throws IOException {
		OpisnikProstoraStanja rezultat = new OpisnikProstoraStanja();
		
		InputStream is = new FileInputStream(new File(path));
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
		int brojac = 0;
		String line = br.readLine();
		do {
			if(line == null) break;
			if(line.startsWith("#")) {
				line = br.readLine();
				if (line == null) break;
			}
			if(!line.contains(":") && brojac < 2) {
				if(brojac == 0) {
					rezultat.setPocetnoStanje(line.trim());
					brojac++;
				}else if(brojac == 1) {
					rezultat.setCiljnoStanje(Stream.of(line.split(" ")).collect(Collectors.toSet())); 
					brojac++;
				}
			}
			if(line.contains(":")) {
				if(line.split(":").length < 2) {
					rezultat.dodajPrijelaz(line.split(":")[0], new LinkedList<>());
				}else {
					String key = line.split(":")[0].trim();
					String[] succ = line.split(":")[1].trim().split(" ");
					List<Pair> prijelazi = Stream.of(succ).map(el -> {
						String[] pair = el.split(",");
						if(pair.length == 2) {
							return new Pair(pair[0], Double.parseDouble(pair[1]));
						}
						return null;
					})
						.sorted(new Comparator<Pair>() {

							@Override
							public int compare(Pair o1, Pair o2) {
								if(o1.getStanje() instanceof String) {
									return o1.getStanje().compareTo(o2.getStanje());
								}
								return Double.compare(o1.getCijenaPrijelaza(), o2.getCijenaPrijelaza());
							}

						})
						.collect(Collectors.toList());
					rezultat.dodajPrijelaz(key, prijelazi);
				}
			}
			line = br.readLine();
		}while(line != null);
		
		rezultat.setVisited(rezultat.getPrijelazi().keySet().stream().collect(Collectors.toMap(str -> str, str -> false)));
		
		return rezultat;
	}
}
