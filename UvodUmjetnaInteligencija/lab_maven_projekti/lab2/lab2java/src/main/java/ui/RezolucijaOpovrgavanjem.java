package ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class RezolucijaOpovrgavanjem {

	public static List<Klauzula> klauzule;
	public static int numerator = 0;
	public static Set<Pair<Integer, Integer>> prosao = new HashSet<>();
	public static List<Klauzula> P;
	public static List<Klauzula> SoS;
	public static int i;

	public static Map<Klauzula, Pair<Klauzula, Klauzula>> povijest = new HashMap<>();
	public static Set<Klauzula> ispis = new TreeSet<>();
	public static Klauzula zadnja;

	public static List<Pair<Klauzula, String>> komande;

	public static void pokreniAlgoritam(String popisKlauzula, String naredbe) {
		try {
			ucitajPopisKlauzula(popisKlauzula);
			ucitajPopisNaredbi(naredbe);
		} catch(IOException e) {
			System.out.println("Neuspješno čitanje podataka.");
		}

		P = new ArrayList<>(klauzule);
		SoS = new ArrayList<>();

		for(Pair<Klauzula, String> p : komande) {
			Klauzula k = p.getFirst();
			k.setNumeracija(++numerator);
			String command = p.getSecond();

			switch(command) {
				case "?":
					SoS.addAll(k.negiraj());
					opovrgni2(k);
					SoS = new ArrayList<>();
					break;
				case "+":
					P.add(k);
					break;
				case "-":
					P.remove(k);
					break;
				default:
					break;
			}
		}
	}

	public static void pokreniAlgoritam(String path) {
		try {
			ucitajPodatke(path);
		} catch(IOException e) {
			System.out.println("Neuspješno čitanje podataka.");
		}

		P = new ArrayList<>(klauzule.subList(0, klauzule.size() - 1));
		SoS = new ArrayList<>();
		List<Klauzula> lis = klauzule.get(klauzule.size() - 1).negiraj();

		for(Klauzula k : lis) {
			k.setIzvedena(true);
			povijest.put(k, new Pair<Klauzula, Klauzula>(null, null));
		}
		SoS.addAll(lis);

		opovrgni();
	}

	private static void opovrgni() {
		boolean pronasao = false;
		i = 0;
		while(!pronasao) {
			ocisti();
			if(i >= SoS.size()) {
				System.out.println(String.format("[CONCLUSION]: %s is unknown", klauzule.get(klauzule.size() - 1).toStringBezNumeracije()));
				break;
			}
			Klauzula A = SoS.get(i);
			while(true) {
				Klauzula B = nadiDruguKlauzulu(A, SoS, P);

				if(B == null) {
					i++;
					break;
				}

				Klauzula R = izracunajRezultantu(A, B);
				if(R == null) {
					pronasao = true;
					ispisi_poredak(zadnja);
					int ps = 1;

					List<Klauzula> pocetne = ispis.stream().filter(k -> k.isIzvedena() == false).sorted().collect(Collectors.toList());
					List<Klauzula> izvedene = ispis.stream().filter(k -> k.isIzvedena() == true).sorted().collect(Collectors.toList());

					for(Klauzula k : pocetne) {
						k.setNumeracija(ps++);
						System.out.println(k);
					}

					System.out.println("===============");

					for(Klauzula k : izvedene) {
						k.setNumeracija(ps++);
						System.out.println(k);
					}
					System.out.println(String.format("[CONCLUSION]: %s is true", klauzule.get(klauzule.size() - 1).toStringBezNumeracije()));
					break;
				} else {
					R.setIzvedena(true);
					povijest.put(R, new Pair<Klauzula, Klauzula>(A, B));
					prosao.add(new Pair<Integer, Integer>(A.numeracija, B.numeracija));
					prosao.add(new Pair<Integer, Integer>(B.numeracija, A.numeracija));

					if(!SoS.contains(R) && !P.contains(R)) {
						SoS.add(R);
					}
				}

				zadnja = R;
			}
		}
	}

	private static void ispisi_poredak(Klauzula r) {
		if(povijest.get(r) == null) {
			return;
		}
		Pair<Klauzula, Klauzula> parRoditelja = povijest.get(r);
		if(parRoditelja.first == null && parRoditelja.second == null) {
			ispis.add(r);
			return;
		}

		ispisi_poredak(parRoditelja.first);
		ispisi_poredak(parRoditelja.second);
	}

	private static void opovrgni2(Klauzula k) {
		boolean pronasao = false;
		i = 0;
		while(!pronasao) {
			ocisti();
			if(i >= SoS.size()) {
				System.out.println(String.format("[CONCLUSION]: %s is unknown", k.toStringBezNumeracije()));
				break;
			}
			Klauzula A = SoS.get(i);
			while(true) {
				Klauzula B = nadiDruguKlauzulu(A, SoS, P);

				if(B == null) {
					i++;
					break;
				}

				Klauzula R = izracunajRezultantu(A, B);
				if(R == null) {
					pronasao = true;
					System.out.println(String.format("[CONCLUSION]: %s is true", k.toStringBezNumeracije()));
					break;
				} else {
					System.out.println(R);
					prosao.add(new Pair<Integer, Integer>(A.numeracija, B.numeracija));
					prosao.add(new Pair<Integer, Integer>(B.numeracija, A.numeracija));

					if(!SoS.contains(R) && !P.contains(R)) {
						SoS.add(R);
					}
				}
			}
		}
	}

	private static void ocisti() {
		List<Klauzula> remove = new ArrayList<>();
		for(Klauzula k : P) {
			for(Klauzula k2 : SoS) {
				if(k.jePredanaKlauzulaRedundantna(k2)) {
					remove.add(k2);
					if(SoS.indexOf(k2) < i) {
						i--;
					}
					//System.out.println("Ukloni zbog redundancije: " + k2);
				}
			}
		}

		SoS.removeAll(remove);
		remove = new ArrayList<>();

		for(Klauzula k : SoS) {
			for(Klauzula k2 : SoS) {
				if(!k.equals(k2) && k.jePredanaKlauzulaRedundantna(k2)) {
					remove.add(k2);
					if(SoS.indexOf(k2) < i) {
						i--;
					}
					//System.out.println("Ukloni zbog redundancije: " + k2);
				}
			}
		}

		SoS.removeAll(remove);
		remove = new ArrayList<>();

		for(Klauzula k : P) {
			if(k.vratiKontradikcije(k).size() != 0) {
				remove.add(k);

				//System.out.println("Ukloni zbog kontradikcije: " + k);
			}
		}

		P.removeAll(remove);
		remove = new ArrayList<>();

		for(Klauzula k : SoS) {
			if(k.vratiKontradikcije(k).size() != 0) {
				remove.add(k);
				if(SoS.indexOf(k) < i) {
					i--;
				}
				//System.out.println("Ukloni zbog kontradikcije: " + k);
			}
		}

		SoS.removeAll(remove);
	}

	private static Klauzula izracunajRezultantu(Klauzula a, Klauzula b) {
		Set<Literal> rez = new HashSet<>();
		List<String> literali = a.vratiKontradikcije(b);
		for(Literal l : a.getLiterali()) {
			if(l.getSimbol().equals(literali.get(0))) {
				continue;
			}
			rez.add(l);
		}

		for(Literal l : b.getLiterali()) {
			if(l.getSimbol().equals(literali.get(0))) {
				continue;
			}
			rez.add(l);
		}

		if(rez.size() == 0) {
			return null;
		} else {
			Klauzula kla = new Klauzula(rez);
			kla.setIzvedena(true);
			kla.setNumeracija(++numerator);
			kla.setRoditelji(new Pair<Integer, Integer>(a.getNumeracija(), b.getNumeracija()));

			return kla;
		}
	}

	private static Klauzula nadiDruguKlauzulu(Klauzula a, List<Klauzula> sos, List<Klauzula> p) {
		for(Klauzula klauzula : p) {
			if(!prosao.contains(new Pair<Integer, Integer>(a.numeracija, klauzula.numeracija)) && a.vratiKontradikcije(klauzula).size() != 0) {
				return klauzula;
			}
		}

		for(Klauzula klauzula : sos) {
			if(!a.equals(klauzula) && !prosao.contains(new Pair<Integer, Integer>(a.numeracija, klauzula.numeracija)) && a.vratiKontradikcije(klauzula).size() != 0) {
				return klauzula;
			}
		}

		return null;
	}

	private static void ucitajPodatke(String path) throws IOException {
		InputStream is = new FileInputStream(new File(path));
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		klauzule = br.lines().filter((line) -> !line.trim().startsWith("#"))
				.map(line -> line.toLowerCase())
				.map(line -> line.split(" v "))
				.map(polje -> {
					Set<Literal> literali = new HashSet<>();
					for(String literal : polje) {
						if(literal.trim().startsWith("~")) {
							literali.add(new Literal(literal.trim().substring(1), true));
						} else {
							literali.add(new Literal(literal.trim(), false));
						}
					}

					return new Klauzula(literali);
				}).collect(Collectors.toList());

		for(int i = 1; i < klauzule.size(); i++) {
			klauzule.get(i - 1).setNumeracija(i);
			klauzule.get(i - 1).setIzvedena(false);
			klauzule.get(i - 1).setRoditelji(new Pair<Integer, Integer>(null, null));
			povijest.put(klauzule.get(i - 1), new Pair<Klauzula, Klauzula>(null, null));
		}

		numerator = klauzule.size() - 1;
	}

	private static void ucitajPopisNaredbi(String naredbe) throws FileNotFoundException {
		InputStream is = new FileInputStream(new File(naredbe));
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		komande = br.lines().filter((line) -> !line.trim().startsWith("#"))
				.map(line -> line.toLowerCase())
				.map(line -> new Pair<String, String>(line.substring(0, line.lastIndexOf(" ")), line.substring(line.lastIndexOf(" ") + 1)))
				.map(pair -> {
					Set<Literal> literali = new HashSet<>();
					for(String literal : pair.first.split(" v ")) {
						if(literal.trim().startsWith("~")) {
							literali.add(new Literal(literal.trim().substring(1), true));
						} else {
							literali.add(new Literal(literal.trim(), false));
						}
					}

					Klauzula k = new Klauzula(literali);

					return new Pair<Klauzula, String>(k, pair.second);
				}).collect(Collectors.toList());
	}

	private static void ucitajPopisKlauzula(String popisKlauzula) throws FileNotFoundException {
		InputStream is = new FileInputStream(new File(popisKlauzula));
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		klauzule = br.lines().filter((line) -> !line.trim().startsWith("#"))
				.map(line -> line.toLowerCase())
				.map(line -> line.split(" v "))
				.map(polje -> {
					Set<Literal> literali = new HashSet<>();
					for(String literal : polje) {
						if(literal.trim().startsWith("~")) {
							literali.add(new Literal(literal.trim().substring(1), true));
						} else {
							literali.add(new Literal(literal.trim(), false));
						}
					}

					return new Klauzula(literali);
				}).collect(Collectors.toList());

		for(int i = 1; i <= klauzule.size(); i++) {
			klauzule.get(i - 1).setNumeracija(i);
		}

		numerator = klauzule.size();
	}
}
