package ui;

import java.util.List;

public class Zapis {
	int redniBrojZapisa;
	List<String> vrijednosti;
	String cilj;
	
	public Zapis(int redniBrojZapisa, List<String> vrijednosti, String cilj) {
		super();
		this.redniBrojZapisa = redniBrojZapisa;
		this.vrijednosti = vrijednosti;
		this.cilj = cilj;
	}

	public int getRedniBrojZapisa() {
		return redniBrojZapisa;
	}

	public void setRedniBrojZapisa(int redniBrojZapisa) {
		this.redniBrojZapisa = redniBrojZapisa;
	}

	public List<String> getVrijednosti() {
		return vrijednosti;
	}

	public void setVrijednosti(List<String> vrijednosti) {
		this.vrijednosti = vrijednosti;
	}

	public String getCilj() {
		return cilj;
	}

	public void setCilj(String cilj) {
		this.cilj = cilj;
	}
}
