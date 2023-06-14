package progi.projekt.backend.service;

import java.util.List;

import progi.projekt.backend.model.Klijent;
import progi.projekt.backend.model.Klub;
import progi.projekt.backend.model.Prijava;

public interface PrijavaServis {

	Prijava stvoriPrijavu(Prijava novi);

	Prijava dohvatiPrijavu(Klijent trener, Klub klub);

	Prijava potvrdiPrijavu(Prijava prijavaFromDB);

	List<Prijava> dohvatiSvePotvrdenePrijavePoKlubu(Long klubId);

	List<Prijava> dohvatiSveNepotvrdenePrijavePoKlubu(Long klubId);

	List<Prijava> dohvatiSvePotvrdenePrijavePoTreneru(Klijent trener);

}
