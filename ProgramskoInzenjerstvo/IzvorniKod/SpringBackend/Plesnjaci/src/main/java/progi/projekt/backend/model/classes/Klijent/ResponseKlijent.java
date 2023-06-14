package progi.projekt.backend.model.classes.Klijent;

import java.util.List;

import progi.projekt.backend.model.Klijent;

public class ResponseKlijent{
	Klijent user;
	List<Klijent> listaKlijenata;
	boolean success;
	String message;
	
	public ResponseKlijent(Klijent user, List<Klijent> listaKlijenata, boolean success,String message) {
		super();
		this.user=user;
		this.listaKlijenata = listaKlijenata;
		this.success=success;
		this.message=message;
	}

	public Klijent getUser() {
		return user;
	}

	public void setUser(Klijent user) {
		this.user = user;
	}

	public List<Klijent> getListaKlijenata() {
		return listaKlijenata;
	}

	public void setListaKlijenata(List<Klijent> listaKlijenata) {
		this.listaKlijenata = listaKlijenata;
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