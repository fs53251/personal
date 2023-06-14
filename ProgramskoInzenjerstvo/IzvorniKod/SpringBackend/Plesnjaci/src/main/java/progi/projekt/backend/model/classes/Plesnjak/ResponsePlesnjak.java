package progi.projekt.backend.model.classes.Plesnjak;

import java.util.List;

import progi.projekt.backend.model.Plesnjak;

public class ResponsePlesnjak {
	Plesnjak plesnjak;
	List<Plesnjak> listaPlesnjaka;
	boolean success;
	String message;
	public ResponsePlesnjak(Plesnjak plesnjak, List<Plesnjak> listaPlesnjaka, boolean success, String message) {
		super();
		this.plesnjak = plesnjak;
		this.listaPlesnjaka = listaPlesnjaka;
		this.success = success;
		this.message = message;
	}
	public Plesnjak getPlesnjak() {
		return plesnjak;
	}
	public void setPlesnjak(Plesnjak plesnjak) {
		this.plesnjak = plesnjak;
	}
	public List<Plesnjak> getListaPlesnjaka() {
		return listaPlesnjaka;
	}
	public void setListaPlesnjaka(List<Plesnjak> listaPlesnjaka) {
		this.listaPlesnjaka = listaPlesnjaka;
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
