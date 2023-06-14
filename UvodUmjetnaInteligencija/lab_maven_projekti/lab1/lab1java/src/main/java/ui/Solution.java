package ui;

/**
 * Pocetni razred rjesenja. Korisnik bira izmedu algoritama BFS, UCS i A*.
 * Razred omogucava dodatnu provjeru konzistentnosti i optimisticnost.
 * 
 * Za izradu vjezbe su koristeni pseudokodovi dani na predavanjima i 
 * materijalima FERWeba ovog predmeta: slideovi predavanja, skripta predavanja http://java.zemris.fer.hr/nastava/ui/.
 * 
 * @author filip
 *
 */
public class Solution {

	/**
	 * Glavni razred main. Instancira razred AlgoritmiZadataka koji nudi sve funkcionalnosti 
	 * za trazene algoritme. Main sluzi parsiranju ulaza, a posao delegira drugim razredima 
	 * ovisno o korinikovom unosu.
	 * @param args korisnikov unos
	 */
	public static void main(String ... args) {
		AlgoritmiZadatka algorithms = new AlgoritmiZadatka();
		String alg = null;
		String ss = null;
		String h = null;
		boolean checkOptimistic = false;
		boolean checkConsistent = false;
		for(int i = 0; i < args.length; i++) {
			switch(args[i]) {
			case "--alg":
				alg = args[i + 1];
				break;
			case "--ss":
				ss = args[i + 1];
				break;
			case "--h":
				h = args[i + 1];
				break;
			case "--check-optimistic":
				checkOptimistic = true;
				break;
			case "--check-consistent":
				checkConsistent = true;
				break;
			default:
				break;
			}
		}
		
		if(alg == null) {
			if(checkConsistent) {
				algorithms.checkConsistent(ss, h);
			}else if(checkOptimistic) {
				algorithms.checkOptimistic(ss, h);
			}
		}else {
			switch(alg) {
			case "bfs":
				algorithms.BFS(ss);
				break;
			case "ucs":
				algorithms.UCS(ss);
				break;
			case "astar":
				if(checkConsistent) {
					algorithms.checkConsistent(ss, h);
				}else if(checkOptimistic) {
					algorithms.checkOptimistic(ss, h);
				}else {
					algorithms.ASTAR(ss, h);
				}
			}
		}
	}

}
