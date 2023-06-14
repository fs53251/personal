package ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Klauzula implements Comparable<Klauzula> {
	Integer numeracija;
	Set<Literal> literali;
	Pair<Integer, Integer> roditelji;
	boolean izvedena = true;

	public Klauzula(Set<Literal> literali) {
		super();
		this.literali = literali;
	}

	public boolean isIzvedena() {
		return izvedena;
	}

	public void setIzvedena(boolean izvedena) {
		this.izvedena = izvedena;
	}

	public Integer getNumeracija() {
		return numeracija;
	}

	public void setNumeracija(Integer numeracija) {
		this.numeracija = numeracija;
	}

	public Set<Literal> getLiterali() {
		return literali;
	}

	public void setLiterali(Set<Literal> literali) {
		this.literali = literali;
	}

	public Pair<Integer, Integer> getRoditelji() {
		return roditelji;
	}

	public void setRoditelji(Pair<Integer, Integer> roditelji) {
		this.roditelji = roditelji;
	}

	public List<String> vratiKontradikcije(Klauzula druga) {
		List<String> rezultat = new ArrayList<>();
		for(Literal l : literali) {
			for(Literal l1 : druga.literali) {
				if(l.jeKontradikcija(l1)) {
					rezultat.add(l.getSimbol());
					continue;
				}
			}
		}

		return rezultat;
	}

	public boolean jePredanaKlauzulaRedundantna(Klauzula k) {
		int size = 0;
		for(Literal l : this.literali) {
			if(k.literali.contains(l)) {
				size++;

			}
		}
		return size == this.literali.size();
	}

	public List<Klauzula> negiraj() {
		List<Klauzula> rez = new ArrayList<>();
		for(Literal l : this.literali) {
			Set<Literal> s = new HashSet<>();
			s.add(new Literal(l.getSimbol(), !l.negacija));
			Klauzula nova = new Klauzula(s);
			nova.setIzvedena(this.izvedena);
			nova.setNumeracija(++RezolucijaOpovrgavanjem.numerator);
			rez.add(nova);
		}
		return rez;
	}

	@Override
	public int hashCode() {
		return Objects.hash(literali);
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		Klauzula other = (Klauzula) obj;
		return Objects.equals(literali, other.literali);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.numeracija + ". ");
		for(Literal l : this.literali) {
			if(l.jeNegiran()) {
				sb.append("~" + l.getSimbol());
			} else {
				sb.append(l.getSimbol());
			}
			sb.append(" v ");
		}

		String ispis = sb.toString();
		ispis = ispis.substring(0, ispis.length() - 3);
		if(roditelji == null) {
			return ispis;
		} else {
			return izvedena ? ispis + " (" + roditelji.getFirst() + ", " + roditelji.getSecond() + ")" : ispis;
		}
	}

	public String toStringBezNumeracije() {
		StringBuilder sb = new StringBuilder();
		for(Literal l : this.literali) {
			if(l.jeNegiran()) {
				sb.append("~" + l.getSimbol());
			} else {
				sb.append(l.getSimbol());
			}
			sb.append(" v ");
		}

		String ispis = sb.toString();
		ispis = ispis.substring(0, ispis.length() - 3);
		if(roditelji == null) {
			return ispis;
		} else {
			return izvedena ? ispis + " (" + roditelji.getFirst() + ", " + roditelji.getSecond() + ")" : ispis;
		}

	}

	@Override
	public int compareTo(Klauzula o) {
		return this.numeracija.compareTo(o.numeracija);
	}

}
