package progi.projekt.backend.model.classes.Termin;

import java.util.List;

import progi.projekt.backend.model.Termin;

public class TerminiKlijentTrener {
	List<Termin> terminiTrener;
	List<Termin> terminiKlijent;
	boolean success;
	String message;
	public TerminiKlijentTrener(List<Termin> terminiTrener, List<Termin> terminiKlijent, boolean success,
			String message) {
		super();
		this.terminiTrener = terminiTrener;
		this.terminiKlijent = terminiKlijent;
		this.success = success;
		this.message = message;
	}
	public List<Termin> getTerminiTrener() {
		return terminiTrener;
	}
	public void setTerminiTrener(List<Termin> terminiTrener) {
		this.terminiTrener = terminiTrener;
	}
	public List<Termin> getTerminiKlijent() {
		return terminiKlijent;
	}
	public void setTerminiKlijent(List<Termin> terminiKlijent) {
		this.terminiKlijent = terminiKlijent;
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
