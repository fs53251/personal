package progi.projekt.backend.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import progi.projekt.backend.model.Klijent;
import progi.projekt.backend.model.Klub;
import progi.projekt.backend.model.Prijava;
import progi.projekt.backend.repository.PrijavaRepository;
import progi.projekt.backend.service.PrijavaServis;

@Service
public class PrijavaImpl implements PrijavaServis{
	
	PrijavaRepository prijavaRepository;
	
	public PrijavaImpl(@Autowired PrijavaRepository prijavaRepository) {
		this.prijavaRepository = prijavaRepository; 
	}

	@Override
	public Prijava dohvatiPrijavu(Klijent trener, Klub klub) {
		return prijavaRepository.dohvatPrijave(trener.getKlijentId(), klub.getKlubId()); 
	}
	
	@Override
	public Prijava stvoriPrijavu(Prijava novi) {
		Prijava prijava = dohvatiPrijavu(novi.getKlijent(), novi.getKlub());
		//update ako vec postoji prijava 
		if(prijava != null) {
			//return prijavaRepository.saveAndFlush(novi); 
			return null; 
		}
		return prijavaRepository.save(novi); 
	}
	
	@Override
	public Prijava potvrdiPrijavu(Prijava prijavaFromDB) {
		return prijavaRepository.saveAndFlush(prijavaFromDB); 
	}
	
	@Override
	public List<Prijava> dohvatiSvePotvrdenePrijavePoKlubu(Long klubId) {
		return prijavaRepository.sviPotvrdeniPoKlubu(klubId); 
	}
	@Override
	public List<Prijava> dohvatiSveNepotvrdenePrijavePoKlubu(Long klubId) {
		return prijavaRepository.sviNepotvrdeniPoKlubu(klubId); 
	}

	@Override
	public List<Prijava> dohvatiSvePotvrdenePrijavePoTreneru(Klijent trener) {
		return prijavaRepository.svePotvrdenePoTreneru(trener.getKlijentId());  
	}

	public Prijava obrisiPrijavu(Klijent trener, Klub klub) {
		Prijava prijava = prijavaRepository.dohvatPrijave(trener.getKlijentId(), klub.getKlubId()); 
		
		if(prijava != null) {
			prijavaRepository.deleteById(prijava.getPrijavaId());;
			return prijava; 
		}
		return null; 
	}
	
}
