package progi.projekt.backend.model.classes.Plesnjak;

import java.util.List;

import progi.projekt.backend.model.Plesnjak;

public class PlesnjakPles {
	Plesnjak newPlesnjak;
	List<String> tipoviPlesa;
	public PlesnjakPles(Plesnjak newPlesnjak, List<String> tipoviPlesa) {
		super();
		this.newPlesnjak = newPlesnjak;
		this.tipoviPlesa = tipoviPlesa;
	}
	public Plesnjak getNewPlesnjak() {
		return newPlesnjak;
	}
	public void setNewPlesnjak(Plesnjak newPlesnjak) {
		this.newPlesnjak = newPlesnjak;
	}
	public List<String> getTipoviPlesa() {
		return tipoviPlesa;
	}
	public void setTipoviPlesa(List<String> tipoviPlesa) {
		this.tipoviPlesa = tipoviPlesa;
	}
	
	
}