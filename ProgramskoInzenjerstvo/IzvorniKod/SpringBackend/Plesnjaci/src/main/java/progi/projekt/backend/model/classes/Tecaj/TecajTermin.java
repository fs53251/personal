package progi.projekt.backend.model.classes.Tecaj;

import java.util.List;

import progi.projekt.backend.model.Tecaj;
import progi.projekt.backend.model.classes.Termin.VrijemeDvorana;

public class TecajTermin {
	private Tecaj tecaj;
	private List<VrijemeDvorana> termini;
	public TecajTermin(Tecaj tecaj, List<VrijemeDvorana> termini) {
		super();
		this.tecaj = tecaj;
		this.termini = termini;
	}
	
	public TecajTermin() {
		
	}

	public Tecaj getTecaj() {
		return tecaj;
	}

	public void setTecaj(Tecaj tecaj) {
		this.tecaj = tecaj;
	}

	public List<VrijemeDvorana> getTermini() {
		return termini;
	}

	public void setTermini(List<VrijemeDvorana> termini) {
		this.termini = termini;
	}
	
	
}
