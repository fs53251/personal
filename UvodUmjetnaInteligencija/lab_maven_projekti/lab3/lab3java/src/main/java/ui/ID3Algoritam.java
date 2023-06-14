package ui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.TreeSet;

public class ID3Algoritam {
	static int razina = 0;
	static Set<String> ciljevi = new HashSet<>();
	static boolean prviPoziv = false;
	
	public static Cvor id3(Model model, Integer ogranicenje) {
		if(!prviPoziv) {
			ciljevi = model.getSviCiljevi();
			prviPoziv = true;
		}
		
		razina++;
		/**
		 * Ako je skup primjeraka S prazan, tada se stvara klasifikacijski cvor koji primjerke klasificira
		 * u razred koji je najcesi u skupu primjeraka koji je razmatrao neposredni roditelj.
		 */
		if(model.getS().size() == 0) {
			String najcesciRazred = izvediNajcesiRazred(model.getRoditeljskiModel());
			
			KlasifikacijskiCvor kc = new KlasifikacijskiCvor(model, null, razina, najcesciRazred);
			razina--;
			
			return kc;
		}
		
		
		/**
		 * Ako više nema atributa koji nisu razmatrani u nekom od roditelja (sve do korijena), stvara se 
		 * klasifikacijski cvor koji primjercima pridjeljuje razred koji je najcesi u analiziranom skupu S.
		 */
		if(model.brojNeobradenihAtributa() == 0) {
			String najcesciRazred = izvediNajcesiRazred(model);
			
			KlasifikacijskiCvor kc = new KlasifikacijskiCvor(model, null, razina, najcesciRazred);
			razina--;
			
			return kc;
		}
		
		/**
		 * Ako svi primjerci u promatranom skupu S pripadaju istom razredu (oznacimo taj razred kao r), algoritam stvara klasifikacijski 
		 * cvor koji primjercima dodjeljuje razred r i tu rekurzija staje.
		 */
		if(model.sviCiljevi.size() == 1) {
			KlasifikacijskiCvor kc = new KlasifikacijskiCvor(model, null, razina, model.sviCiljevi.iterator().next());
			razina--;
			
			return kc;
		}
		
		String sljedeciAtributZaObradu = nadiAtribut(model);
		
		/**
		 * Algoritam odabire atribut koji daje maksimalnu informacijsku dobit, stvara cvor koji razmatra vrijednost tog atributa 
		 * te za svaku razlicitu vrijednost atributa dodaje po jedno dijete koje gradi rekurzivnim pozivom 
		 * uz podskup skupa S u kojem se nalaze samo oni primjerci kojima je odabrani atribut postavljen na razmatranu vrijednost.
		 */
		Map<String, Cvor> djeca = new HashMap<>();
		Set<String> vrijednostiAtributa = model.sveVrijednostiAtributa.get(sljedeciAtributZaObradu);
		Iterator<String> it = vrijednostiAtributa.iterator();
		
		while(it.hasNext()) {
			String vrijednost = it.next();
			
			List<Zapis> podskup = izracunajPodskupS(model, vrijednost, sljedeciAtributZaObradu);
			Model modelDijete = new Model(model.atributi, podskup, model);
			for( Entry<String, Boolean> obraden: model.obradeniAtributi.entrySet()) {
				if(obraden.getValue()) {
					modelDijete.obradiAtribut(obraden.getKey());
				}
			}
			modelDijete.obradiAtribut(sljedeciAtributZaObradu);
			
			Cvor dijete = id3(modelDijete, ogranicenje);
			
			if(ogranicenje != null && ogranicenje == dijete.getRazina() - 2) {
					Map<String, Integer> tmpBrojac = new HashMap<>();
	
					for(Zapis zapis : model.getS()) {
						tmpBrojac.put(zapis.getCilj(), tmpBrojac.getOrDefault(zapis.getCilj(), 0) + 1);
					}
					
					String vrijednostCilja = tmpBrojac.entrySet().stream().max((entry1, entry2) -> {
						int cmp;
						if((cmp = Integer.compare(entry1.getValue(), entry2.getValue())) == 0) {
							return entry2.getKey().compareTo(entry1.getKey());
						}
						return cmp;
					}).get().getKey();
					
					KlasifikacijskiCvor kc = new KlasifikacijskiCvor(model, null, razina, vrijednostCilja);
					razina--;
					
					return kc;
			}
			djeca.put(vrijednost, dijete);
		}

		UnutarnjiCvor unutarnji = new UnutarnjiCvor(model, null, razina, djeca, sljedeciAtributZaObradu);
		for(Entry<String, Cvor> d : djeca.entrySet()) {
			d.getValue().setRoditelj(unutarnji);
		}
		
		razina--;
		return unutarnji;
	}

	private static String nadiAtribut(Model model) {
		Map<String, Double> informacijskaDobit = new HashMap<>();
		
		for(String atribut : model.izdvojiNeobradeneAtribute()) {
			Iterator<String> it = model.sveVrijednostiAtributa.get(atribut).iterator();
			List<Double> tmpRez = new LinkedList<>();
			
			while(it.hasNext()) {
				String vrijednost = it.next();
				
				List<Zapis> podskupS = izracunajPodskupS(model, vrijednost, atribut);
				List<Double> rez = new LinkedList<>();
				Iterator<String> it1 = ciljevi.iterator();
				
				while(it1.hasNext()) {
					String izvuciRazred = it1.next();
					
					long brojPojavljivanjaRazredaPodskup = izracunajBrojPojavljivanjaRazreda(izvuciRazred, podskupS);
					rez.add(brojPojavljivanjaRazredaPodskup / (double)podskupS.size());
				}
				
				double entropijaPodskupa = 0;
				for(Double pi : rez) {
					if(pi != 0) {
						entropijaPodskupa -= pi * (Math.log(pi) / Math.log(2));
					}
				}
				tmpRez.add((-1) * entropijaPodskupa * (podskupS.size() / (double) model.getS().size()));
			}
			
			double ID = izracunajEntropijuPocetnogSkupa(model);
			for(Double d : tmpRez) {
				ID += d;
			}
			
			informacijskaDobit.put(atribut, ID);
		}
		informacijskaDobit.entrySet().stream().forEach(el -> System.out.printf("IG(" + el.getKey() +")=" + el.getValue() + " "));
		System.out.println();
		
		 Entry<String, Double> ispis = informacijskaDobit.entrySet().stream().max((entry1, entry2) -> {
			int cmp;
			if((cmp = Double.compare(entry1.getValue(), entry2.getValue())) == 0) {
				return entry2.getKey().compareTo(entry1.getKey());
			}
			return cmp;
		}).get();
		
		return ispis.getKey();
	}

	private static double izracunajEntropijuPocetnogSkupa(Model model) {
		final int brojElemenataS = model.getS().size();
		Iterator<String> it = ciljevi.iterator();
		Map<String, Integer> rez = new HashMap<>();
		
		while(it.hasNext()) {
			String cilj = it.next();
			for(Zapis zapis : model.getS()) {
				if(zapis.getCilj().equals(cilj)) {
					rez.put(cilj, rez.getOrDefault(cilj, 0) + 1);
				}
			}
		}
		
		return rez.entrySet().stream().mapToDouble(entry -> entry.getValue() / (double) brojElemenataS).map(p -> (-1) * p * (Math.log(p) / Math.log(2))).sum();
	}

	private static long izracunajBrojPojavljivanjaRazreda(String izvuciRazred, List<Zapis> podskupS) {
		return podskupS.stream().filter(zapis -> zapis.getCilj().equals(izvuciRazred)).count();
	}

	private static List<Zapis> izracunajPodskupS(Model model, String vrijednost, String atribut) {
		
		int indexAtributa = 0;
		for(int i = 0; i < model.getAtributi().size(); i++) {
			if(model.getAtributi().get(i).equals(atribut)) {
				indexAtributa = i;
				break;
			}
		}
		final int tmp = indexAtributa;
		
		return model.getS().stream().filter(zapis -> vrijednost.equals(zapis.getVrijednosti().get(tmp))).collect(Collectors.toList());
	}

	private static String izvediNajcesiRazred(Model model) {
		Set<String> isti = new TreeSet<String>();
		Map<String, Integer> brojPojavljivanjaRazreda = izracunajBrojPojavljivanjaRazreda(model);
		final Integer najveciBroj = izvuciNajveciIzMape(brojPojavljivanjaRazreda);
		
		brojPojavljivanjaRazreda.entrySet().stream()
					.forEach(el -> {
						if(el.getValue() == najveciBroj) {
							isti.add(el.getKey());
						}
					});
		
		return isti.iterator().next();
	}

	private static Integer izvuciNajveciIzMape(Map<String, Integer> brojPojavljivanjaRazreda) {
		int i = Integer.MIN_VALUE;
		
		for(Entry<String, Integer> element : brojPojavljivanjaRazreda.entrySet()) {
			if(element.getValue() > i) {
				i = element.getValue();
			}
		}
		
		return i;
	}

	private static Map<String, Integer> izracunajBrojPojavljivanjaRazreda(Model model) {
		Map<String, Integer> rez = new HashMap<String, Integer>();
		List<Zapis> skupS = model.getS();
		
		for(int i = 0; i < skupS.size(); i++) {
			String cilj = skupS.get(i).getCilj();
			int brojPojavljivanja = rez.getOrDefault(cilj, 0);
			rez.put(cilj, ++brojPojavljivanja);
		}
		
		return rez;
	}
}
