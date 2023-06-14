package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;
/**
 * Klasa koja implementira kompleksni broj.
 * @author filip
 *
 */
public class Complex {

	public static final Complex ZERO = new Complex(0, 0);
	public static final Complex ONE = new Complex(1, 0);
	public static final Complex ONE_NEG = new Complex(-1, 0);
	public static final Complex IM = new Complex(0, 1);
	public static final Complex IM_NEG = new Complex(0, -1);

	public double real;
	public double imaginary;
	
	/**
	 * konstruktor
	 * @param real
	 * @param imaginary
	 */
	public Complex(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}
	
	public Complex() {
		this(0., 0.);
	}
	
	/**
	 * Metoda racuna modul kompleksnog broja. Korijen sume realnog i imaginarnog dijela na kvadrat.
	 * @return
	 */
	public double module() {
		double real_kvadrat = Math.pow(this.real, 2);
		double imaginary_kvadrat = Math.pow(this.imaginary, 2);
		return Math.pow(real_kvadrat + imaginary_kvadrat, 0.5);
	}

	/**
	 * Metoda racuna umnozak dva kompleksna broja.
	 * @param c
	 * @return
	 */
	public Complex multiply(Complex c) {
		return new Complex((this.real * c.real) - (this.imaginary * c.imaginary), 
							(this.real * c.imaginary) + (c.real * this.imaginary));
	}
	
	/**
	 * Metoda dijeli dva kompleksna broja.
	 * @param c
	 * @return
	 */
	public Complex divide(Complex c) {
		return new Complex(((this.real * c.real) + (this.imaginary * c.imaginary)) / (Math.pow(c.real, 2) + Math.pow(c.imaginary, 2)),
							((this.imaginary * c.real) - (this.real * c.imaginary)) / (Math.pow(c.real, 2) + Math.pow(c.imaginary, 2)));
	}

	/**
	 * Metoda racuna zbroj dva kompleksna broja.
	 * @param c
	 * @return
	 */
	public Complex add(Complex c) {
		return new Complex(c.real + this.real, c.imaginary + this.imaginary);
	}

	/**
	 * Metoda racuna razliku dva kompleksna broja.
	 * @param c
	 * @return
	 */
	public Complex sub(Complex c) {
		return new Complex(this.real - c.real, this.imaginary - c.imaginary);
	}

	
	/**
	 * Metoda racuna negaciju kompleksnog broja.
	 * @param c
	 * @return
	 */
	public Complex negate() {
		return new Complex(this.real * (-1), this.imaginary * (-1));
	}

	/**
	 * Metoda racuna n tu potenciju kompleknsog broja.
	 * @param c
	 * @return
	 */
	public Complex power(int n) {
		Complex rez = this;
		for (int i = 0; i < n-1; i++) {
			rez = rez.multiply(this);
		}
		return rez;
	}

	/**
	 * meotda racuna korijene kompleksnog broja.
	 * @param n
	 * @return
	 */
	public List<Complex> root(int n) {
		List<Complex> rez = new ArrayList<>();
		double korijenN = Math.pow(this.module(), 1 / (double)n);
		double kut= Math.atan(this.imaginary / this.real);
		//korijenN*(cos(kut + 2 * k * pi)/ n + i * sin(kut + 2 * k * pi) / n)  k = 0, 1, 2... n-1
		for(int k = 0; k <= n - 1; k++) {
			rez.add(new Complex(korijenN * Math.cos((kut + 2 * k * Math.PI) / n), korijenN * Math.sin((kut + 2 * k * Math.PI) / n)));
		}
		
		return rez;
	}

	@Override
	public String toString() {
		return Double.toString(this.real) + (this.imaginary > 0 ? "+i" : "-i") + Double.toString(this.imaginary);
	}
}
