package progi.projekt.backend.model.classes.Klub;

import java.util.List;

import progi.projekt.backend.model.Klub;

public class ResponseKlub {
	Klub klub; 
	List<Klub> listaKlubova; 
	boolean success; 
	String message; 
	
	public ResponseKlub(Klub klub, List<Klub> listaKlubova, boolean success, String message) {
		super(); 
		this.klub = klub; 
		this.listaKlubova = listaKlubova;
		this.success = success; 
		this.message = message; 
	}

	public Klub getKlub() {
		return klub;
	}

	public void setKlub(Klub klub) {
		this.klub = klub;
	}

	public List<Klub> getListaKlubova() {
		return listaKlubova;
	}

	public void setListaKlubova(List<Klub> listaKlubova) {
		this.listaKlubova = listaKlubova;
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
