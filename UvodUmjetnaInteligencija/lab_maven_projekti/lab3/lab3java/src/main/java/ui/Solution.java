package ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


public class Solution {
	public static void main(String... args) {
		String dirFile = args[0];
		String dirTestFile = args[1];
		
		Integer ogranicenje = null;
		if(args.length == 3) {
			ogranicenje = Integer.parseInt(args[2]);
		}
		
		try {
			ucitajPodatke(dirFile, dirTestFile, ogranicenje);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void ucitajPodatke(String dirFile, String dirTestFile, Integer ogranicenje) throws IOException {
		//ucitaj dirFile
		InputStream is = new FileInputStream(new File(dirFile));
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		boolean prvi = false;
		String line;
		int brojZapisa = 1;
		List<String> atributi = new LinkedList<>();
		List<Zapis> S = new LinkedList<>();
		
		
		while((line = br.readLine()) != null) {
			String[] red = line.split(",");
			
			if(!prvi) {
				for(int i = 0; i < red.length - 1; i++) {
					atributi.add(red[i]);
				}
				prvi = true;
			}else {
				List<String> vrijednosti = new LinkedList<>();
				
				for(int i = 0; i < red.length - 1; i++) {
					vrijednosti.add(red[i]);
				}
				
				S.add(new Zapis(brojZapisa++, vrijednosti, red[red.length - 1]));
			}
		}
		
		Cvor stablo = stvoriStablo(atributi, S, ogranicenje);
		
		//ucitaj testne primjere
		InputStream isTest = new FileInputStream(new File(dirTestFile));
		BufferedReader brTest = new BufferedReader(new InputStreamReader(isTest));
		boolean prviTest = false;
		String lineTest;
		int brojZapisaTest = 1;
		List<Zapis> STest = new LinkedList<>();
		
		while((lineTest = brTest.readLine()) != null) {
			String[] red = lineTest.split(",");
			
			if(!prviTest) {
				prviTest = true;
			}else {
				List<String> vrijednosti = new LinkedList<>();
				
				for(int i = 0; i < red.length - 1; i++) {
					vrijednosti.add(red[i]);
				}
				
				STest.add(new Zapis(brojZapisaTest++, vrijednosti, red[red.length - 1]));
			}
		}
		
		izvediPredikcije(STest, atributi, stablo);
	}

	private static void izvediPredikcije(List<Zapis> sTest, List<String> atributi, Cvor stablo) {
		Map<String, Integer> indexAtributa = new HashMap<>();
		for(int i = 0; i < atributi.size(); i++) {
			indexAtributa.put(atributi.get(i), i);
		}
		
		Set<String> ciljevi = new TreeSet<>();
		for(Zapis z : sTest) {
			ciljevi.add(z.getCilj());
		}
		Map<Pair<String, String>, Integer> matrica = new HashMap<>();
		for(String c1 : ciljevi) {
			for(String c2 : ciljevi) {
				matrica.put(new Pair<String, String>(c1, c2), 0);
			}
		}

		int brojacUspjelih = 0;
		System.out.printf("[PREDICTIONS]:");
		for(Zapis zapis : sTest) {
			List<String> vrijednosti = zapis.vrijednosti;
			String rez = stablo.obilazakStabla(indexAtributa, vrijednosti);
			
			Pair<String, String> tmpPair = new Pair<String, String>(zapis.getCilj(), rez);
			matrica.put(tmpPair, matrica.getOrDefault(tmpPair, 0) + 1);
			
			
			if(rez.equals(zapis.getCilj())){
				brojacUspjelih++;
			}
			System.out.printf(" %s", rez);
		}
		System.out.printf("\n[ACCURACY]: %.5f\n", brojacUspjelih / (double) sTest.size());
		
		System.out.println("[CONFUSION_MATRIX]:");
		for(String c1 : ciljevi) {
			for(String c2 : ciljevi) {
				System.out.printf("%d ", matrica.get(new Pair<String, String>(c1, c2)));
			}
			System.out.println();
		}
	}

	private static Cvor stvoriStablo(List<String> atributi, List<Zapis> s, Integer ogranicenje) {
		Model model = new Model(atributi, s, null);
		Cvor stablo = ID3Algoritam.id3(model, ogranicenje);
		
		System.out.println(stablo);
		
		return stablo;
	}
}
