package hr.fer.zemris.math;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
/**
 * Predstavlja kompleksni polinom.
 * @author filip
 *
 */
public class ComplexPolynomial {
	Complex[] faktori;
	
	// constructor
	public ComplexPolynomial(Complex ...factors) {
		this.faktori = factors;
	}
	// returns order of this polynom; eg. For (7+2i)z^3+2z^2+5z+1 returns 3
	/**
	 * Metoda vraća stupanj kompleksnog polinoma.
	 * @return
	 */
	public short order() {
		int n = this.faktori.length - 1;
		return (short) n;
	}
	// computes a new polynomial this*p
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		Complex[] rez = new Complex[this.faktori.length + p.faktori.length -1];
		for(int i = 0; i < rez.length; i++) {
			rez[i] = new Complex();
		}
		for(int prvi = 0; prvi < this.faktori.length; prvi++) {
			for(int drugi = 0; drugi < p.faktori.length; drugi++) {
				rez[prvi + drugi] = rez[prvi + drugi].add(this.faktori[prvi].multiply(p.faktori[drugi]));
			}
		}
		return new ComplexPolynomial(rez);
	}
	// computes first derivative of this polynomial; for example, for
	// (7+2i)z^3+2z^2+5z+1 returns (21+6i)z^2+4z+5
	/**
	 * Metoda vraća derivaciju kompleksnog polinoma
	 * @return
	 */
	public ComplexPolynomial derive() {
		Complex[] rez = new Complex[this.faktori.length - 1];
		for(int i = 0; i < rez.length; i++) {
			//pomnoziti faktor s potencijom 
			Complex potencija = new Complex(i + 1, 0);
			rez[i] = this.faktori[i + 1].multiply(potencija);
		}
		return new ComplexPolynomial(rez);
	}
	// computes polynomial value at given point z
	/**
	 * Izracun vrijednosti za kompleksni polinom
	 * @param z
	 * @return
	 */
	public Complex apply(Complex z) {
		Complex rez = this.faktori[0];
		for(int i = 1; i < this.faktori.length; i++) {
			rez = rez.add(faktori[i].multiply(z.power(i)));
		}
		return rez;
	}
	@Override
	public String toString() {
		List<Complex> polje = Arrays.asList(Arrays.copyOf(this.faktori, this.faktori.length));
		Collections.reverse(polje);
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < polje.size() - 1; i++) {
			int potencija = polje.size() - i - 1;
			String s = "(" + polje.get(i).toString() + ")" + "*z^" + potencija + "+";
			sb.append(s);
		}
		sb.append("(" + polje.get(polje.size() - 1).toString() + ")");
		return sb.toString();
	}
}