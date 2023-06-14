package ui;

import java.util.Map;

public class UnutarnjiCvor extends Cvor{
	
	Map<String, Cvor> djeca;
	String atribut;

	public UnutarnjiCvor(Model model, Cvor roditelj, int razina) {
		super(model, roditelj, razina);
	}
	
	
	public UnutarnjiCvor(Model model, Cvor roditelj, int razina, Map<String, Cvor> djeca, String atribut) {
		super(model, roditelj, razina);
		this.djeca = djeca;
		this.atribut = atribut;
	}


	public Map<String, Cvor> getDjeca() {
		return djeca;
	}


	public void setDjeca(Map<String, Cvor> djeca) {
		this.djeca = djeca;
	}


	public String getAtribut() {
		return atribut;
	}


	public void setAtribut(String atribut) {
		this.atribut = atribut;
	}
	
	
}
