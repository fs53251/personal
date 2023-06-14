package ui;

public class KlasifikacijskiCvor extends Cvor{
	String cilj;
	
	public KlasifikacijskiCvor(Model model, Cvor roditelj, int razina, String cilj) {
		super(model, roditelj, razina);
		this.cilj = cilj;
	}

	public String getCilj() {
		return cilj;
	}

	public void setCilj(String cilj) {
		this.cilj = cilj;
	}
}
