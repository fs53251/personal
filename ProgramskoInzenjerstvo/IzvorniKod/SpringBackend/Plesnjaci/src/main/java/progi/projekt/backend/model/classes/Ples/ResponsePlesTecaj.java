package progi.projekt.backend.model.classes.Ples;

import java.util.List;


public class ResponsePlesTecaj {
	List<String> Plesovi;
	boolean success;
	String message;
	public ResponsePlesTecaj(List<String> plesovi, boolean success, String message) {
		super();
		Plesovi = plesovi;
		this.success = success;
		this.message = message;
	}
	public List<String> getPlesovi() {
		return Plesovi;
	}
	public void setPlesovi(List<String> plesovi) {
		Plesovi = plesovi;
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
