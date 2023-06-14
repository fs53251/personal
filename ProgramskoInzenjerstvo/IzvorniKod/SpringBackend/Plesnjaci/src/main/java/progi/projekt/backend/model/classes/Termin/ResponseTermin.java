package progi.projekt.backend.model.classes.Termin;

import java.util.List;

import progi.projekt.backend.model.Termin;

public class ResponseTermin {
	Termin termin;
	List<Termin> listaTermina;
	boolean success;
	String message;
	public ResponseTermin(Termin termin, List<Termin> listaTermina, boolean success, String message) {
		super();
		this.termin = termin;
		this.listaTermina = listaTermina;
		this.success = success;
		this.message = message;
	}
	public Termin getTermin() {
		return termin;
	}
	public void setTermin(Termin termin) {
		this.termin = termin;
	}
	public List<Termin> getListaTermina() {
		return listaTermina;
	}
	public void setListaTermina(List<Termin> listaTermina) {
		this.listaTermina = listaTermina;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
