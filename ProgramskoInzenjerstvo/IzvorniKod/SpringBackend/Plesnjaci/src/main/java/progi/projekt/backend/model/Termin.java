package progi.projekt.backend.model;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="TERMIN")
public class Termin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long terminId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
	private Date vrijeme;
	
	@ManyToOne()
	private Tecaj tecaj;
	
	@ManyToOne()
	private Dvorana dvorana;

	public Termin(Date vrijeme, Tecaj tecaj, Dvorana dvorana) {
		super();
		this.vrijeme = vrijeme;
		this.tecaj = tecaj;
		this.dvorana = dvorana;
	}
	
	public Termin() {

	}
	
	public Long getTerminId() {
		return terminId;
	}

	public Date getVrijeme() {
		return vrijeme;
	}

	public void setVrijeme(Date vrijeme) {
		this.vrijeme = vrijeme;
	}

	public Tecaj getTecaj() {
		return tecaj;
	}

	public void setTecaj(Tecaj tecaj) {
		this.tecaj = tecaj;
	}

	public Dvorana getDvorana() {
		return dvorana;
	}

	public void setDvorana(Dvorana dvorana) {
		this.dvorana = dvorana;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dvorana, terminId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Termin other = (Termin) obj;
		return Objects.equals(dvorana, other.dvorana) && Objects.equals(terminId, other.terminId);
	}

	
}
