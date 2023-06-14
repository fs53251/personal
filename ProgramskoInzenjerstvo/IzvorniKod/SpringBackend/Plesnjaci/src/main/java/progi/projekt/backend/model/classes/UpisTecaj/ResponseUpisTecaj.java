package progi.projekt.backend.model.classes.UpisTecaj;

import java.util.List;

import progi.projekt.backend.model.UpisTecaj;

public class ResponseUpisTecaj {
	UpisTecaj upisTecaj;
	List<UpisTecaj> listaUpisaTecaja;
	boolean success;
	String message;
	public ResponseUpisTecaj(UpisTecaj upisTecaj, List<UpisTecaj> listaUpisaTecaja, boolean success, String message) {
		super();
		this.upisTecaj = upisTecaj;
		this.listaUpisaTecaja = listaUpisaTecaja;
		this.success = success;
		this.message = message;
	}
	public UpisTecaj getUpisTecaj() {
		return upisTecaj;
	}
	public void setUpisTecaj(UpisTecaj upisTecaj) {
		this.upisTecaj = upisTecaj;
	}
	public List<UpisTecaj> getListaUpisaTecaja() {
		return listaUpisaTecaja;
	}
	public void setListaUpisaTecaja(List<UpisTecaj> listaUpisaTecaja) {
		this.listaUpisaTecaja = listaUpisaTecaja;
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
