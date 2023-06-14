package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * Iscrtava uzorak koristeći višedretvenost.
 * @author filip
 *
 */
public class NewtonP1 {
	static int brojRoot = 1;
	static ComplexPolynomial polynomial;
	static ComplexPolynomial derived;
	static ComplexRootedPolynomial crp;

	static int numOfWorkers = Runtime.getRuntime().availableProcessors();
	static int numOfJobs = -1;

	/**
	 * Metoda obraduje unos kompleksnih brojeva. Pokrece iscrtavanje naredbom "done",
	 * @param args
	 */
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
	public static class PosaoIzracuna implements Runnable {
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
		 * Svaka dretva dobi određeni segment (nekoliko redaka) i obavlja iscrtavanje.
		 */
		@Override
		public void run() {
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
		if(args.length == 1) {
			// --workers=  ... --tracks= ... 
			if(args[0].startsWith("--workers=")) {
				try {
					Integer broj = Integer.parseInt(args[0].substring(10));
					numOfWorkers = broj;
				} catch(Exception e) {
					System.out.println("Krivi unos za parametar workers. Postavljena default vrijesnot.");
				}

			} else if(args[0].startsWith("--tracks=")) {
				try {
					Integer broj = Integer.parseInt(args[0].substring(9));
					numOfJobs = broj;
				} catch(Exception e) {
					System.out.println("Krivi unos za parametar tracks. Postavljena default vrijesnot.");
				}
			} else {
				System.out.println("Krivi unos parametara. Postavljena default vrijesnot.");
			}
		} else if(args.length == 2) {
			//--workers= ...  --tracks= ...
			//-tracks= ... --workers= ...
			// -w N
			// -t N
			if(args[0].startsWith("--workers=") && args[1].startsWith("--tracks=")) {
				try {
					Integer broj = Integer.parseInt(args[0].substring(10));
					numOfWorkers = broj;
				} catch(Exception e) {
					System.out.println("Krivi unos za parametar workers. Postavljena default vrijednost.");
				}

				try {
					Integer broj = Integer.parseInt(args[1].substring(9));
					numOfJobs = broj;
				} catch(Exception e) {
					System.out.println("Krivi unos za parametar tracks. Postavljena default vrijednost.");
				}
			} else if(args[0].startsWith("--tracks=") && args[1].startsWith("--workers=")) {
				try {
					Integer broj = Integer.parseInt(args[0].substring(9));
					numOfJobs = broj;
				} catch(Exception e) {
					System.out.println("Krivi unos za parametar tracks. Postavljena default vrijednost.");
				}

				try {
					Integer broj = Integer.parseInt(args[1].substring(10));
					numOfWorkers = broj;
				} catch(Exception e) {
					System.out.println("Krivi unos za parametar workers. Postavljena default vrijednost.");
				}
			} else if(args[0].startsWith("-w")) {
				try {
					Integer broj = Integer.parseInt(args[1]);
					numOfWorkers = broj;
				} catch(Exception e) {
					System.out.println("Krivi unos za parametar workers. Postavljena defalult vrijednost.");
				}
			} else if(args[0].startsWith("-t")) {
				try {
					Integer broj = Integer.parseInt(args[1]);
					numOfJobs = broj;
				} catch(Exception e) {
					System.out.println("Krivi unos za parametar tracks. Postavljena default vrijednost.");
				}
			} else {
				System.out.println("Krivi unos parametara. Postavljena default vrijesnot.");
			}
		} else if(args.length == 3) {
			//--workers= ... -t N
			//--tracks= ... -w N
			if(args[0].startsWith("--workers=") && args[1].startsWith("-t")) {
				try {
					Integer broj = Integer.parseInt(args[0].substring(10));
					numOfWorkers = broj;
				} catch(Exception e) {
					System.out.println("Krivi unos za parametar workers. Postavljena default vrijednost.");
				}
				try {
					Integer broj = Integer.parseInt(args[2]);
					numOfJobs = broj;
				} catch(Exception e) {
					System.out.println("Krivi unos za parametar tracks. Postavljena default vrijednost.");
				}
			} else if(args[0].startsWith("--tracks=") && args[1].startsWith("-w")) {
				try {
					Integer broj = Integer.parseInt(args[0].substring(9));
					numOfJobs = broj;
				} catch(Exception e) {
					System.out.println("Krivi unos za parametar tracks. Postavljena default vrijednost.");
				}
				try {
					Integer broj = Integer.parseInt(args[2]);
					numOfWorkers = broj;
				} catch(Exception e) {
					System.out.println("Krivi unos za parametar workers. Postavljena defalult vrijednost.");
				}
			} else {
				System.out.println("Krivi unos parametara. Postavljena default vrijesnot.");
			}
		} else if(args.length == 4) {
			//-w N -t N
			//-t N -w N
			if(args[0].startsWith("-w") && args[2].startsWith("-t")) {
				try {
					Integer broj = Integer.parseInt(args[1]);
					numOfWorkers = broj;
				} catch(Exception e) {
					System.out.println("Krivi unos za parametar workers. Postavljena defalult vrijednost.");
				}
				try {
					Integer broj = Integer.parseInt(args[3]);
					numOfJobs = broj;
				} catch(Exception e) {
					System.out.println("Krivi unos za parametar tracks. Postavljena default vrijednost.");
				}
			} else if(args[0].startsWith("-t") && args[2].startsWith("-w")) {
				try {
					Integer broj = Integer.parseInt(args[3]);
					numOfWorkers = broj;
				} catch(Exception e) {
					System.out.println("Krivi unos za parametar workers. Postavljena defalult vrijednost.");
				}
				try {
					Integer broj = Integer.parseInt(args[1]);
					numOfJobs = broj;
				} catch(Exception e) {
					System.out.println("Krivi unos za parametar tracks. Postavljena default vrijednost.");
				}
			} else {
				System.out.println("Krivi unos parametara. Postavljena default vrijesnot.");
			}
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
	 * Razred implementira dano sucelje IFractalProducer. Koristi ExecutorService za zadavanje poslova, jer
	 * se glavni posao dijeli na podposlove, dijeleci ekran na segmente.
	 * Zadavanjem posla ExecutorService-u, spremamo listu opisnika tih poslova i cekamo kraj izvrsavanja svih
	 * poslova.
	 * @author filip
	 *
	 */
	public static class MojProducer implements IFractalProducer {

		ExecutorService pool;
		List<Future<?>> rezultati;
		int heightUsed;

		boolean izvedenaMetodaSetup = false;

		@Override
		public synchronized void close() {
			pool.shutdown();
			rezultati = null;
			izvedenaMetodaSetup = false;
		}

		@Override
		public synchronized void produce(double reMin, double reMax, double imMin, double imMax, int width, int height, long requestNo,
				IFractalResultObserver observer, AtomicBoolean cancel) {

			if(!izvedenaMetodaSetup) {
				heightUsed = height;
				setup();
				izvedenaMetodaSetup = true;
			}

			int m = 16 * 16 * 16;
			short[] data = new short[width * height];

			int brojYPoTraci = height / numOfJobs;

			for(int i = 0; i < numOfJobs; i++) {
				int yMin = i * brojYPoTraci;
				int yMax = (i + 1) * brojYPoTraci;
				if(i == numOfJobs - 1) {
					yMax = height - 1;
				}

				PosaoIzracuna posao = new PosaoIzracuna(reMin, reMax, imMin, imMax, width, height, yMin, yMax, m, data, cancel);
				rezultati.add(pool.submit(posao));
			}

			for(Future<?> f : rezultati) {
				while(true) {
					try {
						f.get();
						break;
					} catch(InterruptedException | ExecutionException e) {}
				}
			}

			observer.acceptResult(data, (short) (polynomial.order() + 1), requestNo);
		}

		@Override
		public void setup() {
			if(numOfJobs > heightUsed) {
				numOfJobs = heightUsed;
			}
			if(numOfJobs == -1) {
				numOfJobs = 4 * numOfWorkers;
			}
			System.out.println("Koristim " + numOfWorkers + " procesa.");
			System.out.println("Koristim " + numOfJobs + " traka.");

			pool = Executors.newFixedThreadPool(numOfWorkers);
			rezultati = new ArrayList<>();
		}

	}
}
