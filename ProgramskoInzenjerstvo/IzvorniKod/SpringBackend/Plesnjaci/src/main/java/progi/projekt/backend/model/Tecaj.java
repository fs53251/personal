package progi.projekt.backend.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="TECAJ")
public class Tecaj {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long tecajId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date rokPrijave;
	
	private int kapacitetGrupe;
	
	private String opis;
	
	private String ogranicenja;
	
	@ManyToOne()
	private Klijent trener;
	
	@ManyToOne()
	private Klub klub;
	
	@ManyToOne()
	private TipPlesa tipPlesa;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tecaj", cascade={CascadeType.PERSIST, CascadeType.MERGE, 
	           CascadeType.REFRESH}, orphanRemoval = true)
	private List<Termin> termini = new ArrayList<>();

	public Tecaj(Long tecajId, Date rokPrijave, int kapacitetGrupe, String opis, String ogranicenja, 
			Klijent trener, Klub klub, TipPlesa tipPlesa, List<Termin> termini) {
		super();
		this.tecajId = tecajId;
		this.rokPrijave = rokPrijave;
		this.kapacitetGrupe = kapacitetGrupe;
		this.opis = opis;
		this.ogranicenja = ogranicenja;
		this.trener = trener;
		this.klub = klub;
		this.tipPlesa = tipPlesa;
		this.termini = termini;
	}
	
	public Tecaj() {
		
	}

	
	public List<Termin> getTermini() {
		return termini;
	}

	public void setTermini(List<Termin> termini) {
		this.termini.addAll(termini);
	}

	public Long getTecajId() {
		return tecajId;
	}

	public void setTecajId(Long tecajId) {
		this.tecajId = tecajId;
	}

	public Date getRokPrijave() {
		return rokPrijave;
	}

	public void setRokPrijave(Date rokPrijave) {
		this.rokPrijave = rokPrijave;
	}

	public int getKapacitetGrupe() {
		return kapacitetGrupe;
	}

	public void setKapacitetGrupe(int kapacitetGrupe) {
		this.kapacitetGrupe = kapacitetGrupe;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public String getOgranicenja() {
		return ogranicenja;
	}

	public void setOgranicenja(String ogranicenja) {
		this.ogranicenja = ogranicenja;
	}

	public Klijent getTrener() {
		return trener;
	}

	public void setTrener(Klijent trener) {
		this.trener = trener;
	}

	public Klub getKlub() {
		return klub;
	}

	public void setKlub(Klub klub) {
		this.klub = klub;
	}

	public TipPlesa getTipPlesa() {
		return tipPlesa;
	}

	public void setTipPlesa(TipPlesa tipPlesa) {
		this.tipPlesa = tipPlesa;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tecajId == null) ? 0 : tecajId.hashCode());
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
		Tecaj other = (Tecaj) obj;
		if (tecajId == null) {
			if (other.tecajId != null)
				return false;
		} else if (!tecajId.equals(other.tecajId))
			return false;
		return true;
	}
}
