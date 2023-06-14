package progi.projekt.backend.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import progi.projekt.backend.model.Klijent;
import progi.projekt.backend.model.Klub;
import progi.projekt.backend.repository.KlubRepository;
import progi.projekt.backend.service.KlubServis;

@Service
public class KlubImpl implements KlubServis{

	KlubRepository klubRepository; 
	
	public KlubImpl(@Autowired KlubRepository klubRepository) {
		this.klubRepository = klubRepository; 
	}
	
	@Override
	public Klub dohvatiKlub(String imeKluba, Klijent vlasnik) {
		return klubRepository.dohvatKlub(imeKluba, vlasnik.getKorisnicko_ime());
	}
	
	@Override
	public Klub dohvatiKlubPoId(Long klubId) {
		return klubRepository.findById(klubId).get();
	}

	@Override
	public Klub stvoriKlub(Klub novi) {
		Klub klub = dohvatiKlub(novi.getImeKluba(), novi.getVlasnik());
		if(klub != null){
			return null;
		}
		return klubRepository.save(novi); 
	}

	@Override
	public Klub updateKlub(Klub updatedKlub, String imeKluba, Klijent vlasnik) {
		Klub stariKlub = dohvatiKlub(imeKluba, vlasnik);
		
		if(updatedKlub.getImeKluba() != null) {
			stariKlub.setImeKluba(updatedKlub.getImeKluba());
		}
		if(updatedKlub.getEmail() != null) {
			stariKlub.setEmail(updatedKlub.getEmail());
		}
		if(updatedKlub.getOpis() != null) {
			stariKlub.setOpis(updatedKlub.getOpis());
		}
		if(updatedKlub.getPoveznica() != null) {
			stariKlub.setPoveznica(updatedKlub.getPoveznica());
		}
		if(updatedKlub.getTelefon() != null) {
			stariKlub.setTelefon(updatedKlub.getTelefon());
		}
		if(updatedKlub.getDvorana() != null) {
			stariKlub.setDvorana(updatedKlub.getDvorana());
		}
		if(updatedKlub.getVlasnik() != null) {
			stariKlub.setVlasnik(updatedKlub.getVlasnik());
		}
		return klubRepository.saveAndFlush(stariKlub); 
		
	}

	@Override
	public Klub obrisiKlub(String imeKluba, Klijent vlasnik) {
		Klub klub = dohvatiKlub(imeKluba, vlasnik);
		if(klub != null) {
			klubRepository.deleteById(klub.getKlubId());
			return klub;
		}
		return null;
	}

	@Override
	public List<Klub> dohvatiSveKlubove() {
		
		List<Klub> listaKlubova = klubRepository.findAll();  
		return listaKlubova;
	}

	@Override
	public List<Klub> dohvatiSvePotvrdene() {
		return klubRepository.sviPotvrdeni();
	}

	@Override
	public List<Klub> dohvatiSveNepotvrdene() {
		return klubRepository.sviNepotvrdeni();
	}

	@Override
	public Klub potvrdiKlub(Klub klubFromDB) {
		return klubRepository.saveAndFlush(klubFromDB);
	}

	@Override
	public List<Klub> dohvatiKluboveFilterVlasnik(Klijent vlasnik) {
		return klubRepository.filterByVlasnik(vlasnik.getKlijentId());
	} 
	

}