package progi.projekt.backend.model.classes.Ples;

import java.util.List;

import progi.projekt.backend.model.TipPlesa;

public class ResponsePles {
	TipPlesa ples;
	List<TipPlesa> listaPlesova;
	boolean success;
	String message;
	public ResponsePles(TipPlesa ples, List<TipPlesa> listaPlesova, boolean success, String message) {
		super();
		this.ples = ples;
		this.listaPlesova = listaPlesova;
		this.success = success;
		this.message = message;
	}
	
	public TipPlesa getPles() {
		return ples;
	}
	
	public void setPles(TipPlesa ples) {
		this.ples = ples;
	}
	
	public List<TipPlesa> getListaPlesova() {
		return listaPlesova;
	}
	
	public void setListaPlesova(List<TipPlesa> listaPlesova) {
		this.listaPlesova = listaPlesova;
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
