package progi.projekt.backend.model.classes.Dvorana;

import java.util.List;

import progi.projekt.backend.model.Dvorana;

public class ResponseDvorana {
	Dvorana dvorana;
	List<Dvorana> listaDvorana;
	boolean success;
	String message;
	
	public ResponseDvorana(Dvorana dvorana, List<Dvorana> listaDvorana, boolean success, String message) {
		super();
		this.dvorana = dvorana;
		this.listaDvorana = listaDvorana;
		this.success = success;
		this.message = message;
	}

	public Dvorana getDvorana() {
		return dvorana;
	}

	public void setDvorana(Dvorana dvorana) {
		this.dvorana = dvorana;
	}

	public List<Dvorana> getListaDvorana() {
		return listaDvorana;
	}

	public void setListaDvorana(List<Dvorana> listaDvorana) {
		this.listaDvorana = listaDvorana;
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
