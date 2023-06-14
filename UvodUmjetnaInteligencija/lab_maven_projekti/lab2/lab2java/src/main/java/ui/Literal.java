package ui;

import java.util.Objects;

public class Literal {
	String simbol;
	boolean negacija;
	
	public Literal(String simbol, boolean negacija) {
		super();
		this.simbol = simbol;
		this.negacija = negacija;
	}

	public String getSimbol() {
		return simbol;
	}

	public void setSimbol(String simbol) {
		this.simbol = simbol;
	}

	public boolean jeNegiran() {
		return negacija;
	}

	public void setNegacija(boolean negacija) {
		this.negacija = negacija;
	}
	
	public boolean jeKontradikcija(Literal drugi) {
		return this.simbol.equals(drugi.simbol) && this.negacija != drugi.negacija;
	}

	@Override
	public int hashCode() {
		return Objects.hash(negacija, simbol);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Literal other = (Literal) obj;
		return negacija == other.negacija && Objects.equals(simbol, other.simbol);
	}
}
