package progi.projekt.backend.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="PRIJAVA")
public class Prijava {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long prijavaId;
	
	@ManyToOne
	private Klijent klijent;
	
	@ManyToOne
	private Klub klub;
	
	private String motivacijskoPismo;
	
	private String potvrda;
	
	private boolean jePotvrden;

	public Prijava(Klijent klijent, Klub klub, String motivacijskoPismo, String potvrda, boolean jePotvrden) {
		super();
		this.klijent = klijent;
		this.klub = klub;
		this.motivacijskoPismo = motivacijskoPismo;
		this.potvrda = potvrda;
		this.jePotvrden = jePotvrden;
	}
	
	public Prijava() {
		
	}
	
	public Long getPrijavaId() {
		return prijavaId;
	}

	public Klijent getKlijent() {
		return klijent;
	}

	public void setKlijent(Klijent klijent) {
		this.klijent = klijent;
	}

	public Klub getKlub() {
		return klub;
	}

	public void setKlub(Klub klub) {
		this.klub = klub;
	}

	public String getMotivacijskoPismo() {
		return motivacijskoPismo;
	}

	public void setMotivacijskoPismo(String motivacijskoPismo) {
		this.motivacijskoPismo = motivacijskoPismo;
	}

	public String getPotvrda() {
		return potvrda;
	}

	public void setPotvrda(String potvrda) {
		this.potvrda = potvrda;
	}

	public boolean isJePotvrden() {
		return jePotvrden;
	}

	public void setJePotvrden(boolean jePotvrden) {
		this.jePotvrden = jePotvrden;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((klijent == null) ? 0 : klijent.hashCode());
		result = prime * result + ((klub == null) ? 0 : klub.hashCode());
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
		Prijava other = (Prijava) obj;
		if (klijent == null) {
			if (other.klijent != null)
				return false;
		} else if (!klijent.equals(other.klijent))
			return false;
		if (klub == null) {
			if (other.klub != null)
				return false;
		} else if (!klub.equals(other.klub))
			return false;
		return true;
	}
	
	
}
