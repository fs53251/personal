package ui;

import java.util.List;

public class Zapis {
	List<Double> vrijednosti;
	Double cilj;
	
	public Zapis(List<Double> vrijednosti, Double cilj) {
		super();
		this.vrijednosti = vrijednosti;
		this.cilj = cilj;
	}

	public List<Double> getVrijednosti() {
		return vrijednosti;
	}

	public void setVrijednosti(List<Double> vrijednosti) {
		this.vrijednosti = vrijednosti;
	}

	public Double getCilj() {
		return cilj;
	}

	public void setCilj(Double cilj) {
		this.cilj = cilj;
	}

	@Override
	public String toString() {
		return "Zapis [vrijednosti=" + vrijednosti + ", cilj=" + cilj + "]";
	}
	
	
}
