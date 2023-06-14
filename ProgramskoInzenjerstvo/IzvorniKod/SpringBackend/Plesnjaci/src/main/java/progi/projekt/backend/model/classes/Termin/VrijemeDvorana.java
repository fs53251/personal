package progi.projekt.backend.model.classes.Termin;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class VrijemeDvorana {
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
	private Date vrijeme;
	private String adresaDvorane;
	
	public VrijemeDvorana(Date vrijeme, String adresaDvorane) {
		super();
		this.vrijeme = vrijeme;
		this.adresaDvorane = adresaDvorane;
	}
	public Date getVrijeme() {
		return vrijeme;
	}
	public void setVrijeme(Date vrijeme) {
		this.vrijeme = vrijeme;
	}
	public String getAdresaDvorane() {
		return adresaDvorane;
	}
	public void setAdresaDvorane(String adresaDvorane) {
		this.adresaDvorane = adresaDvorane;
	}
	
	
}
