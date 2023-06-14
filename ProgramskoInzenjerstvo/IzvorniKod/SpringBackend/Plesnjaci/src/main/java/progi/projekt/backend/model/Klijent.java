package progi.projekt.backend.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="KLIJENT")
@JsonIgnoreProperties(value= {"handler","hibernateLazyInitializer","FieldHandler"})
public class Klijent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long klijentId;
	
	private String korisnicko_ime;
	
	private String lozinka;
	
	private String ime;
	
	private String prezime;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date datumRodenja;
	
	private String brojMobitela;
	
	private String fotografija;
	
	private Character spol;
	
	private String email;
	
	private String plesnoIskustvo;
	
	private String tipKorisnika;

	public Klijent(String korisnicko_ime, String lozinka, String ime, String prezime, Date datumRodenja,
			String brojMobitela, String fotografija, Character spol, String email, String plesnoIskustvo,
			String tipKorisnika) {
		super();
		this.korisnicko_ime = korisnicko_ime;
		this.lozinka = lozinka;
		this.ime = ime;
		this.prezime = prezime;
		this.datumRodenja = datumRodenja;
		this.brojMobitela = brojMobitela;
		this.fotografija = fotografija;
		this.spol = spol;
		this.email = email;
		this.plesnoIskustvo = plesnoIskustvo;
		this.tipKorisnika = tipKorisnika;
	}
	
	public Klijent() {
		
	}
	
	public Long getKlijentId() {
		return klijentId;
	}

	public String getKorisnicko_ime() {
		return korisnicko_ime;
	}

	public void setKorisnicko_ime(String korisnicko_ime) {
		this.korisnicko_ime = korisnicko_ime;
	}

	public String getLozinka() {
		return lozinka;
	}

	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public Date getDatumRodenja() {
		return datumRodenja;
	}

	public void setDatumRodenja(Date datumRodenja) {
		this.datumRodenja = datumRodenja;
	}

	public String getBrojMobitela() {
		return brojMobitela;
	}

	public void setBrojMobitela(String brojMobitela) {
		this.brojMobitela = brojMobitela;
	}

	public String getFotografija() {
		return fotografija;
	}

	public void setFotografija(String fotografija) {
		this.fotografija = fotografija;
	}

	public Character getSpol() {
		return spol;
	}

	public void setSpol(Character spol) {
		this.spol = spol;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPlesnoIskustvo() {
		return plesnoIskustvo;
	}

	public void setPlesnoIskustvo(String plesnoIskustvo) {
		this.plesnoIskustvo = plesnoIskustvo;
	}

	public String getTipKorisnika() {
		return tipKorisnika;
	}

	public void setTipKorisnika(String tipKorisnika) {
		this.tipKorisnika = tipKorisnika;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((korisnicko_ime == null) ? 0 : korisnicko_ime.hashCode());
		result = prime * result + ((lozinka == null) ? 0 : lozinka.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Klijent other = (Klijent) obj;
		if (korisnicko_ime == null) {
			if (other.korisnicko_ime != null)
				return false;
		} else if (!korisnicko_ime.equals(other.korisnicko_ime))
			return false;
		if (lozinka == null) {
			if (other.lozinka != null)
				return false;
		} else if (!lozinka.equals(other.lozinka))
			return false;
		return true;
	}
}	
	
	