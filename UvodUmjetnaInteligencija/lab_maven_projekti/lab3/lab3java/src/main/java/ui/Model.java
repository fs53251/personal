package ui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Model {
	List<String> atributi;
	Map<String, Boolean> obradeniAtributi;
	List<Zapis> S;
	Map<String, Set<String>> sveVrijednostiAtributa;
	Set<String> sviCiljevi;
	Model roditeljskiModel;
	
	public Model(List<String> atributi, List<Zapis> S, Model roditeljModel) {
		this.atributi = atributi;
		this.S = S;
		this.roditeljskiModel = roditeljModel;
		
		obradeniAtributi = new HashMap<>();
		sveVrijednostiAtributa = new HashMap<>();
		for(int i = 0; i < atributi.size(); i++) {
			String atribut = atributi.get(i);
			obradeniAtributi.put(atribut, false);
			
			Set<String> tmp = new TreeSet<String>();
			for(int j = 0; j < S.size(); j++) {
				String vrijednostAtributa = S.get(j).getVrijednosti().get(i);
				tmp.add(vrijednostAtributa);
			}
			sveVrijednostiAtributa.put(atribut, tmp);
		}
		
		sviCiljevi = new HashSet<>();
		for(int i = 0; i < S.size(); i++) {
			sviCiljevi.add(S.get(i).getCilj());
		}
	}
	
	public void obradiAtribut(String s) {
		obradeniAtributi.put(s, true);
	}
	
	public int brojNeobradenihAtributa() {
		int brojac = 0;
		
		for(Entry<String, Boolean> entry : this.obradeniAtributi.entrySet()) {
			if(entry.getValue() == false) {
				brojac++;
			}
		}
		
		return brojac;
	}

	public List<String> getAtributi() {
		return atributi;
	}

	public void setAtributi(List<String> atributi) {
		this.atributi = atributi;
	}

	public List<Zapis> getS() {
		return S;
	}

	public void setS(List<Zapis> s) {
		S = s;
	}

	public Map<String, Set<String>> getSveVrijednostiAtributa() {
		return sveVrijednostiAtributa;
	}

	public void setSveVrijednostiAtributa(Map<String, Set<String>> sveVrijednostiAtributa) {
		this.sveVrijednostiAtributa = sveVrijednostiAtributa;
	}

	public Set<String> getSviCiljevi() {
		return sviCiljevi;
	}

	public void setSviCiljevi(Set<String> sviCiljevi) {
		this.sviCiljevi = sviCiljevi;
	}

	public Model getRoditeljskiModel() {
		return roditeljskiModel;
	}

	public void setRoditeljskiModel(Model roditeljskiModel) {
		this.roditeljskiModel = roditeljskiModel;
	}
	
	public List<String> izdvojiNeobradeneAtribute(){
		return this.obradeniAtributi.entrySet().stream()
				.filter(e -> e.getValue() == false)
				.map(e -> e.getKey())
				.collect(Collectors.toList());
	}
}
