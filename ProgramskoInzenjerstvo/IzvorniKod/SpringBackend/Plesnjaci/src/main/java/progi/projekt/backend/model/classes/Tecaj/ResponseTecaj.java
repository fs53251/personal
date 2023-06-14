package progi.projekt.backend.model.classes.Tecaj;

import java.util.List;

import progi.projekt.backend.model.Tecaj;

public class ResponseTecaj {
	Tecaj tecaj;
	List<Tecaj> listaTecajeva;
	boolean success;
	String message;
	public ResponseTecaj(Tecaj tecaj, List<Tecaj> listaTecajeva, boolean success, String message) {
		super();
		this.tecaj = tecaj;
		this.listaTecajeva = listaTecajeva;
		this.success = success;
		this.message = message;
	}
	public Tecaj getTecaj() {
		return tecaj;
	}
	public void setTecaj(Tecaj tecaj) {
		this.tecaj = tecaj;
	}
	public List<Tecaj> getListaTecajeva() {
		return listaTecajeva;
	}
	public void setListaTecajeva(List<Tecaj> listaTecajeva) {
		this.listaTecajeva = listaTecajeva;
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
