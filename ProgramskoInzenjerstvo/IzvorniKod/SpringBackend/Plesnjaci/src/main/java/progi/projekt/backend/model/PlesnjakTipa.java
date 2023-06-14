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
@Table(name="PLESNJAK_TIPA")
public class PlesnjakTipa {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long plesnjakTipaId;
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Plesnjak plesnjak;
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	private TipPlesa tipPlesa;

	public PlesnjakTipa(Plesnjak plesnjak, TipPlesa tipPlesa) {
		super();
		this.plesnjak = plesnjak;
		this.tipPlesa = tipPlesa;
	}
	
	public PlesnjakTipa() {
		
	}
	
	public Long getPlesnjakTipaId() {
		return plesnjakTipaId;
	}

	public Plesnjak getPlesnjak() {
		return plesnjak;
	}

	public void setPlesnjak(Plesnjak plesnjak) {
		this.plesnjak = plesnjak;
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
		result = prime * result + ((plesnjak == null) ? 0 : plesnjak.hashCode());
		result = prime * result + ((tipPlesa == null) ? 0 : tipPlesa.hashCode());
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
		PlesnjakTipa other = (PlesnjakTipa) obj;
		if (plesnjak == null) {
			if (other.plesnjak != null)
				return false;
		} else if (!plesnjak.equals(other.plesnjak))
			return false;
		if (tipPlesa == null) {
			if (other.tipPlesa != null)
				return false;
		} else if (!tipPlesa.equals(other.tipPlesa))
			return false;
		return true;
	}
}
