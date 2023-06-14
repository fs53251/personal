package progi.projekt.backend.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name="UPIS_TECAJ")
public class UpisTecaj {	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long upisTecajId;
	
	@ManyToOne()
	private Klijent korisnik;
	
	@ManyToOne()
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Tecaj tecaj;

	private boolean jePotvrden;
	
	public UpisTecaj(Klijent korisnik, Tecaj tecaj, boolean jePotvrden) {
		super();
		this.korisnik = korisnik;
		this.tecaj = tecaj;
		this.jePotvrden = jePotvrden;
	}
	
	public UpisTecaj() {
		
	}
	
	public Long getUpisTecajId() {
		return upisTecajId;
	}
	
	public boolean isJePotvrden() {
		return jePotvrden;
	}

	public void setJePotvrden(boolean jePotvrden) {
		this.jePotvrden = jePotvrden;
	}

	public Klijent getKorisnik() {
		return korisnik;
	}

	public void setKorisnik(Klijent korisnik) {
		this.korisnik = korisnik;
	}

	public Tecaj getTecaj() {
		return tecaj;
	}

	public void setTecaj(Tecaj tecaj) {
		this.tecaj = tecaj;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((korisnik == null) ? 0 : korisnik.hashCode());
		result = prime * result + ((tecaj == null) ? 0 : tecaj.hashCode());
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
		UpisTecaj other = (UpisTecaj) obj;
		if (korisnik == null) {
			if (other.korisnik != null)
				return false;
		} else if (!korisnik.equals(other.korisnik))
			return false;
		if (tecaj == null) {
			if (other.tecaj != null)
				return false;
		} else if (!tecaj.equals(other.tecaj))
			return false;
		return true;
	}
}
