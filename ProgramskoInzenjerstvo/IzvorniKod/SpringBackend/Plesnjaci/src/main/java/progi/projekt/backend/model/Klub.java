package progi.projekt.backend.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name="KLUB")
public class Klub {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long klubId;
	
	private String imeKluba;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vlasnik_korisnicko_ime", nullable = false)
	private Klijent vlasnik;
	
	private String email;
	
	private String telefon;
	
	private boolean jePotvrden;
	
	private String opis;
	
	private String poveznica;
	
	@ManyToOne()
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Dvorana dvorana;

	public Klub(String imeKluba, Klijent vlasnik, String email, String telefon, boolean jePotvrden, String opis,
			String poveznica,Dvorana dvorana) {
		super();
		this.imeKluba = imeKluba;
		this.vlasnik = vlasnik;
		this.email = email;
		this.telefon = telefon;
		this.jePotvrden = jePotvrden;
		this.opis = opis;
		this.poveznica = poveznica;
		this.dvorana = dvorana;
	}
	
	public Klub() {
		
	}
	public void setKlubId(Long klubId) {
		this.klubId = klubId;
	}
	public Long getKlubId() {
		return klubId;
	}

	public String getImeKluba() {
		return imeKluba;
	}

	public void setImeKluba(String imeKluba) {
		this.imeKluba = imeKluba;
	}

	public Klijent getVlasnik() {
		return vlasnik;
	}

	public void setVlasnik(Klijent vlasnik) {
		this.vlasnik = vlasnik;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	public boolean isJePotvrden() {
		return jePotvrden;
	}

	public void setJePotvrden(boolean jePotvrden) {
		this.jePotvrden = jePotvrden;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public String getPoveznica() {
		return poveznica;
	}

	public void setPoveznica(String poveznica) {
		this.poveznica = poveznica;
	}

	public Dvorana getDvorana() {
		return dvorana;
	}

	public void setDvorana(Dvorana dvorana) {
		this.dvorana = dvorana;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((imeKluba == null) ? 0 : imeKluba.hashCode());
		result = prime * result + ((vlasnik == null) ? 0 : vlasnik.hashCode());
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
		Klub other = (Klub) obj;
		if (imeKluba == null) {
			if (other.imeKluba != null)
				return false;
		} else if (!imeKluba.equals(other.imeKluba))
			return false;
		if (vlasnik == null) {
			if (other.vlasnik != null)
				return false;
		} else if (!vlasnik.equals(other.vlasnik))
			return false;
		return true;
	}
	
	
}
