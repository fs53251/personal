package progi.projekt.backend.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TIP_PLESA")
public class TipPlesa {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tipPlesaId;
	
	private String naziv;
	
	private String opis;
	
	private String slika;
	
	private String link;

	public TipPlesa(String naziv, String opis, String slika, String link) {
		super();
		this.naziv = naziv;
		this.opis = opis;
		this.slika = slika;
		this.link = link;
	}
	
	public TipPlesa() {
		
	}
	
	public Long getTipPlesaId() {
		return tipPlesaId;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public String getSlika() {
		return slika;
	}

	public void setSlika(String slika) {
		this.slika = slika;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((naziv == null) ? 0 : naziv.hashCode());
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
		TipPlesa other = (TipPlesa) obj;
		if (naziv == null) {
			if (other.naziv != null)
				return false;
		} else if (!naziv.equals(other.naziv))
			return false;
		return true;
	}
}
