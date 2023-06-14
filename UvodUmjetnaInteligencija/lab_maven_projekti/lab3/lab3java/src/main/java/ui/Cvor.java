package ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Cvor {
	Model model;
	Cvor roditelj;
	int razina;
	
	public Cvor(Model model, Cvor roditelj, int razina) {
		super();
		this.model = model;
		this.roditelj = roditelj;
		this.razina = razina;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public Cvor getRoditelj() {
		return roditelj;
	}

	public void setRoditelj(Cvor roditelj) {
		this.roditelj = roditelj;
	}

	public int getRazina() {
		return razina;
	}

	public void setRazina(int razina) {
		this.razina = razina;
	}
	
	@Override
	public String toString() {
		return "[BRANCHES]:\n" + ispis("", this);
	}

	private String ispis(String odozgo, Cvor c) {
		StringBuilder sb = new StringBuilder();
		if(c instanceof UnutarnjiCvor) {
			UnutarnjiCvor uc = (UnutarnjiCvor) c;
			int razina = uc.getRazina();
			String atribut = uc.getAtribut();
			String dalje = razina + ":" + atribut + "=";
			
			if(!odozgo.equals("")) {
				dalje = odozgo + " " + dalje;
			}
			final String novi = dalje;
			
			for(Entry<String, Cvor> dijete : uc.getDjeca().entrySet()) {
				String redakIspisa = ispis(novi + dijete.getKey(), dijete.getValue());
				if(!(redakIspisa.equals("") || redakIspisa.equals("\n"))) {
					sb.append(redakIspisa);
				}
			}

			
		}else if (c instanceof KlasifikacijskiCvor) {
			KlasifikacijskiCvor kc = (KlasifikacijskiCvor) c;
			odozgo += " " + kc.getCilj() + "\n";
			return odozgo;
		}
		return sb.toString();
	}

	public String obilazakStabla(Map<String, Integer> indexAtributa, List<String> vrijednosti) {
		
		return obilazak(this, indexAtributa, vrijednosti);
	}

	private String obilazak(Cvor c, Map<String, Integer> indexAtributa, List<String> vrijednosti) {
		if(c instanceof UnutarnjiCvor) {
			UnutarnjiCvor uc = (UnutarnjiCvor) c;
			String atribut = uc.getAtribut();
			Integer index = indexAtributa.get(atribut);
			String vrijednostAtributa = vrijednosti.get(index);

			
			if(uc.getDjeca().get(vrijednostAtributa) == null) {
				List<Zapis> zapisi = c.model.S;
				
				Map<String, Integer> tmp = new HashMap<>();
				for(Zapis z : zapisi) {
					tmp.put(z.getCilj(), tmp.getOrDefault(z.getCilj(), 0) + 1);
				}
				 return tmp.entrySet().stream().max((entry1, entry2) -> {
						int cmp;
						if((cmp = Integer.compare(entry1.getValue(), entry2.getValue())) == 0) {
							return entry2.getKey().compareTo(entry1.getKey());
						}
						return cmp;
					}).get().getKey();
			}
			
			return obilazak(uc.getDjeca().get(vrijednostAtributa), indexAtributa, vrijednosti);
			
		}else if (c instanceof KlasifikacijskiCvor) {
			KlasifikacijskiCvor kc = (KlasifikacijskiCvor) c;
			
			return kc.getCilj();
		}
		return null;
	}
	
}
