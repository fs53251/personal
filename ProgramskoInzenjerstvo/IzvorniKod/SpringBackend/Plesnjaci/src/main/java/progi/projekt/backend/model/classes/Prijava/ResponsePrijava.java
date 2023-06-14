package progi.projekt.backend.model.classes.Prijava;

import java.util.List;

import progi.projekt.backend.model.Prijava;

public class ResponsePrijava {
	Prijava prijava;
	List<Prijava> listaPrijava; 
	boolean success; 
	String message; 
	
	public ResponsePrijava(Prijava prijava, List<Prijava> listaPrijava, boolean success, String message) {
		super();
		this.prijava = prijava;
		this.listaPrijava = listaPrijava;
		this.success = success;
		this.message = message;
	}
	
	public Prijava getPrijava() {
		return prijava;
	}
	
	public void setPrijava(Prijava prijava) {
		this.prijava = prijava;
	}
	public List<Prijava> getListaPrijava() {
		return listaPrijava;
	}
	public void setListaPrijava(List<Prijava> listaPrijava) {
		this.listaPrijava = listaPrijava;
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
