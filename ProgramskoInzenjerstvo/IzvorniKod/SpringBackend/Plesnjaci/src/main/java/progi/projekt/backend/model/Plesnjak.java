package progi.projekt.backend.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="PLESNJAK")
public class Plesnjak {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long plesnjakId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date vrijeme;
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Klub klubOrganizator;
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Dvorana dvorana;
	
	private String naziv;
	
	private String slika;
	
	private String opis;
	
	public Plesnjak(Date vrijeme, Klub klubOrganizator, Dvorana dvorana, String naziv, String slika, String opis) {
		super();
		this.vrijeme = vrijeme;
		this.klubOrganizator = klubOrganizator;
		this.dvorana = dvorana;
		this.naziv = naziv;
		this.slika = slika;
		this.opis = opis;
	}
	
	public Plesnjak() {
		
	}
	
	public Long getPlesnjakId() {
		return plesnjakId;
	}

	public Date getVrijeme() {
		return vrijeme;
	}

	public void setVrijeme(Date vrijeme) {
		this.vrijeme = vrijeme;
	}

	public Klub getKlubOrganizator() {
		return klubOrganizator;
	}

	public void setKlubOrganizator(Klub klubOrganizator) {
		this.klubOrganizator = klubOrganizator;
	}

	public Dvorana getDvorana() {
		return dvorana;
	}

	public void setDvorana(Dvorana dvorana) {
		this.dvorana = dvorana;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getSlika() {
		return slika;
	}

	public void setSlika(String slika) {
		this.slika = slika;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dvorana == null) ? 0 : dvorana.hashCode());
		result = prime * result + ((klubOrganizator == null) ? 0 : klubOrganizator.hashCode());
		result = prime * result + ((naziv == null) ? 0 : naziv.hashCode());
		result = prime * result + ((vrijeme == null) ? 0 : vrijeme.hashCode());
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
		Plesnjak other = (Plesnjak) obj;
		if (dvorana == null) {
			if (other.dvorana != null)
				return false;
		} else if (!dvorana.equals(other.dvorana))
			return false;
		if (klubOrganizator == null) {
			if (other.klubOrganizator != null)
				return false;
		} else if (!klubOrganizator.equals(other.klubOrganizator))
			return false;
		if (naziv == null) {
			if (other.naziv != null)
				return false;
		} else if (!naziv.equals(other.naziv))
			return false;
		if (vrijeme == null) {
			if (other.vrijeme != null)
				return false;
		} else if (!vrijeme.equals(other.vrijeme))
			return false;
		return true;
	}
}
