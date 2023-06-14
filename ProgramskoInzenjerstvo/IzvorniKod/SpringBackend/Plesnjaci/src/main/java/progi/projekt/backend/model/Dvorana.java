package progi.projekt.backend.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="DVORANA")
public class Dvorana {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long dvoranaId;
	
	private String adresa;

	public Dvorana(String adresa) {
		super();
		this.adresa = adresa;
	}
	
	public Dvorana() {
		
	}
	
	public Long getDvoranaId() {
		return dvoranaId;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adresa == null) ? 0 : adresa.hashCode());
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
		Dvorana other = (Dvorana) obj;
		if (adresa == null) {
			if (other.adresa != null)
				return false;
		} else if (!adresa.equals(other.adresa))
			return false;
		return true;
	}
}
