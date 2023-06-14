package progi.projekt.backend.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import progi.projekt.backend.model.Klijent;
import progi.projekt.backend.model.Klub;

@Service
@Component
public interface KlubServis {
	Klub dohvatiKlub(String imeKluba, Klijent vlasnik);
	Klub stvoriKlub(Klub novi); 
	Klub updateKlub(Klub updatedKlub, String imeKluba, Klijent vlasnik); 
	Klub obrisiKlub(String imeKluba, Klijent vlasnik); 
	List<Klub> dohvatiSveKlubove(); 
	List<Klub> dohvatiSvePotvrdene();
	List<Klub> dohvatiSveNepotvrdene();
	Klub potvrdiKlub(Klub klub);
	List<Klub> dohvatiKluboveFilterVlasnik(Klijent vlasnik);
	Klub dohvatiKlubPoId(Long klubId);
	
}