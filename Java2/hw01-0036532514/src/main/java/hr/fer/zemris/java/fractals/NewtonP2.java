package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * Iscrtava uzorak koristeći višedretvenost.
 * Obraduje unos kompleksnih brojeva. Pokrece iscrtavanje komandom "done".
 * @author filip
 *
 */
public class NewtonP2 {
	static int brojRoot = 1;
	static ComplexPolynomial polynomial;
	static ComplexPolynomial derived;
	static ComplexRootedPolynomial crp;

	static int numOfWorkers = 4 * Runtime.getRuntime().availableProcessors();
	static int minTracks = -1;

	public static void main(String[] args) {
		obradiArgumente(args);

		System.out.printf("Welcome to Newton-Raphson iteration-based fractal viewer.\n" + "Please enter at least two roots, one root per line. Enter 'done' when done.\n");
		Scanner sc = new Scanner(System.in);
		System.out.printf("Root %d> ", brojRoot);
		String red = null;
		List<Complex> korijeni = new ArrayList<>();
		while(!(red = sc.nextLine()).equals("done")) {
			Complex rez = obradiUnos(red);
			if(rez != null) {
				korijeni.add(rez);
			}

			System.out.printf("Root %d> ", brojRoot);
		}
		sc.close();
		Complex constant = new Complex(1, 0);
		Complex[] root = new Complex[korijeni.size()];
		for(int i = 0; i < root.length; i++) {
			root[i] = korijeni.get(i);
		}
		crp = new ComplexRootedPolynomial(constant, root);
		polynomial = crp.toComplexPolynom();
		derived = polynomial.derive();
		FractalViewer.show(new MojProducer());
	}

	/**
	 * Klasa koja implementira Runnable i metodu run.
	 * Tako svakoj dretvi zadaje određeni posao.
	 * Posao se dijeli između više dretvi.
	 * @author filip
	 *
	 */
	public static class PosaoIzracuna extends RecursiveAction {
		private static final long serialVersionUID = 1L;

		double reMin;
		double reMax;
		double imMin;
		double imMax;
		int width;
		int height;
		int yMin;
		int yMax;
		int m;
		short[] data;
		AtomicBoolean cancel;

		public PosaoIzracuna(double reMin, double reMax, double imMin,
				double imMax, int width, int height, int yMin, int yMax,
				int m, short[] data, AtomicBoolean cancel) {
			super();
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.m = m;
			this.data = data;
			this.cancel = cancel;
		}

		/**
		 * Posao se rekuznivno dijeli segment koji treba obraditi, na pola i tako dok ne dosegne odredenu
		 * granicu nakon koje se pocinje izvoditi posao. Konkretno, dijeli se visina ekrana na manje segmente.
		 */
		@Override
		protected void compute() {
			if(yMax - yMin <= minTracks) {
				izracunajDirektno();
				return;
			}

			int posao1YMIN = yMin;
			int posao1YMAX = yMin + (yMax - yMin) / 2;
			int posao2YMIN = yMin + (yMax - yMin) / 2;
			int posao2YMAX = yMax;

			invokeAll(new PosaoIzracuna(reMin, reMax, imMin, imMax, width, height, posao1YMIN, posao1YMAX, m, data, cancel),
					new PosaoIzracuna(reMin, reMax, imMin, imMax, width, height, posao2YMIN, posao2YMAX, m, data, cancel));

		}

		/**
		 * Metoda koja vrsi izracun nekog jednostavnijeg posla koji je nastao rekurzivnim dijeljenjem u jesnostavnije poslove.
		 */
		private void izracunajDirektno() {
			int offset = yMin * width;
			for(int y = yMin; y < yMax; y++) {
				if(cancel.get())
					break;
				for(int x = 0; x < width; x++) {
					double cre = x / (width - 1.0) * (reMax - reMin) + reMin;
					double cim = (height - 1.0 - y) / (height - 1) * (imMax - imMin) + imMin;

					Complex zn = new Complex(cre, cim);
					double module = 0;
					int iters = 0;
					double treshold = 0.002;
					do {
						Complex numerator = polynomial.apply(zn);
						Complex denominator = derived.apply(zn);
						Complex znold = zn;
						Complex fraction = numerator.divide(denominator);
						zn = zn.sub(fraction);
						module = znold.sub(zn).module();
						iters++;
					}
					while(module > treshold && iters < m);
					data[offset] = (short) ((iters >= m ? iters : crp.indexOfClosestRootFor(zn, treshold)) + 1);
					offset++;
				}
			}
		}
	}

	/**
	 * Parsira se korisnikov unos kroz komandnu liniju.
	 * @param args
	 */
	private static void obradiArgumente(String[] args) {
		//--mintracks=K
		if(args.length == 1) {
			if(args[0].startsWith("--mintracks=")) {
				minTracks = Integer.parseInt(args[0].substring(args[0].indexOf("=") + 1));
			}
		} else if(args.length == 2) { //-m K
			if(args[0].equals("-m")) {
				minTracks = Integer.parseInt(args[1]);
			}
		} else {
			minTracks = 16;
		}
	}

	/**
	 * Parsira se uneseni string u kompleksni broj.
	 * @param red uneseni string.
	 * @return kompleksni broj.
	 */
	private static Complex obradiUnos(String red) {
		if(red.equals("")) {
			System.out.println("Nije unesen legalan kompleksni broj, unesi ponovo:");
			return null;
		}
		String[] unos = red.split(" ");
		int real = 0;
		int imaginary = 0;

		// a + ib ... a - ib
		if(unos.length == 3) {
			int predznak = 1;
			if(unos[1].equals("-")) {
				predznak = -1;
			} else if(!unos[1].equals("+")) {
				System.out.println("krivi unos operacije, unesi ponovo:");
				return null;
			}
			if(unos[2].equals("i")) {
				imaginary = 1 * predznak;
			} else if(unos[2].startsWith("i")) {
				try {
					imaginary = Integer.parseInt(unos[2].substring(1)) * predznak;
				} catch(Exception e) {
					System.out.println("krivi unos imaginarne jedinice, unesi ponovo:");
					return null;
				}
			} else {
				System.out.println("krivi unos imaginarne jedinice, unesi ponovo:");
				return null;
			}

			try {
				real = Integer.parseInt(unos[0]) * predznak;
			} catch(Exception e) {
				System.out.println("krivi unos realnog dijela, unesi ponovo:");
				return null;
			}
			brojRoot++;
			return new Complex(real, imaginary);

		} else if(unos.length == 1) { //i, i2, 24
			int predznak = 1;
			if(unos[0].startsWith("-")) {
				predznak = -1;
				unos[0] = unos[0].substring(1);
			}

			if(unos[0].equals("i")) {
				imaginary = 1 * predznak;
				real = 0;
			} else if(unos[0].startsWith("i")) {
				try {
					imaginary = Integer.parseInt(unos[2].substring(1)) * predznak;
					real = 0;
				} catch(Exception e) {
					System.out.println("krivi unos imaginarne jedinice, unesi ponovo:");
					return null;
				}
			} else if(!unos[0].contains("i")) {
				try {
					real = Integer.parseInt(unos[0]) * predznak;
					imaginary = 0;
				} catch(Exception e) {
					System.out.println("krivi unos realnog dijela, unesi ponovo:");
					return null;
				}
			} else {
				System.out.println("krivi unos, unesi ponovo:");
				return null;
			}
			brojRoot++;
			return new Complex(real, imaginary);
		} else {
			System.out.println("krivi unos, unesi ponovo:");
			return null;
		}
	}

	/**
	 * Statička klasa koja stvara posao. Koristi se ForkJoinPool jer on podrzava rekurzivne akcije i rekurzivne taskove.
	 * @author filip
	 *
	 */
	public static class MojProducer implements IFractalProducer {

		ForkJoinPool pool;

		boolean izvedenaMetodaSetup = false;

		/**
		 * Metoda koja zatvara FrorkJoinPool. Mora biti syncronized jer se close ne smije pozvati dok neka dretva iscrtava 
		 * posao.
		 */
		@Override
		public synchronized void close() {
			pool.shutdown();
			izvedenaMetodaSetup = false;
		}

		/**
		 * Metoda u kojoj se zadaje posao. Zahtjev zadatka je da samo jedna dretva u trenutku bude u metodi produce, stoga
		 * ima synchtonized.
		 */
		@Override
		public synchronized void produce(double reMin, double reMax, double imMin, double imMax, int width, int height, long requestNo,
				IFractalResultObserver observer, AtomicBoolean cancel) {

			if(!izvedenaMetodaSetup) {
				setup();
				izvedenaMetodaSetup = true;
			}

			int m = 16 * 16 * 16;
			short[] data = new short[width * height];

			pool.invoke(new PosaoIzracuna(reMin, reMax, imMin, imMax, width, height, 0, height - 1, m, data, cancel));

			observer.acceptResult(data, (short) (polynomial.order() + 1), requestNo);
		}

		/**
		 * Metoda koja stvara instancu ForkJoinPoola.
		 */
		@Override
		public void setup() {
			pool = new ForkJoinPool();
		}

	}
}
