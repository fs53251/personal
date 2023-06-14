package hr.fer.zemris.math;

/**
 * Klasa koja predstavlja zapis polinoma
 * @author filip
 *
 */
public class ComplexRootedPolynomial {
	Complex[] nultocke;
	Complex z0;
	// constructor
	public ComplexRootedPolynomial(Complex constant, Complex ... roots) {
		this.nultocke = roots;
		this.z0 = constant;
	}
	// computes polynomial value at given point z
	/**
	 * Metoda koja racuana vrijednost polinoma za danu kompleksnu tocku
	 * @param z
	 * @return
	 */
	public Complex apply(Complex z) {
		Complex rez = this.z0;
		for(int i = 0; i < this.nultocke.length; i++) {
			rez = rez.multiply(z.sub(this.nultocke[i]));
		}
		return rez;
	}
	// converts this representation to ComplexPolynomial type
	/**
	 * Pretvara ovaj oblik polinoma u kompleksni tip
	 * @return
	 */
	public ComplexPolynomial toComplexPolynom() {
		//izmnozi z0 * (z - z1)
		//stvori z0 polinom 
		ComplexPolynomial rez = new ComplexPolynomial(z0);
		for(int i = 0; i < nultocke.length; i++) {
			//stvori (z - z1)
			Complex zi_negative = nultocke[i].negate();
			Complex z = new Complex(1, 0);
			ComplexPolynomial z_zi = new ComplexPolynomial(zi_negative, z);
			rez = rez.multiply(z_zi);
		}
		return rez;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(" + z0.toString() + ")");
		for(int i = 0; i < nultocke.length; i++) {
			String s = "*(z-(" + nultocke[i].toString() + "))";
			sb.append(s);
		}
		return sb.toString();
	}
	// finds index of closest root for given complex number z that is within
	// treshold; if there is no such root, returns -1
	// first root has index 0, second index 1, etc
	/**
	 * Racuna najblizu nultocku.
	 * @param z
	 * @param treshold
	 * @return
	 */
	public int indexOfClosestRootFor(Complex z, double treshold) {
		boolean prvi = true;
		int pozicija = 0;
		double minimalni = Double.MAX_VALUE;
		for(int i = 0; i < nultocke.length; i++) {
			//modul: sqrt( (z.re - nultocke[i].re)^2 + (z.im - nultocke[i].im)^2)
			double realni_dio = Math.pow(z.real + nultocke[i].real, 2);
			double imaginarni_dio = Math.pow(z.imaginary - nultocke[i].imaginary, 2);
			double suma = realni_dio + imaginarni_dio;
			double modul = Math.pow(suma, 0.5);
			if(prvi || modul < minimalni) {
				prvi = false;
				minimalni = modul;
				pozicija = i;
			}
		}
		return minimalni <= treshold ? pozicija : -1;
	}
	public static void main(String[] args) {
		ComplexRootedPolynomial crp = new ComplexRootedPolynomial(
				new Complex(2,0), Complex.ONE, Complex.ONE_NEG, Complex.IM, Complex.IM_NEG
				);
				ComplexPolynomial cp = crp.toComplexPolynom();
				System.out.println(crp);
				System.out.println(cp);
				System.out.println(cp.derive());
	}
}