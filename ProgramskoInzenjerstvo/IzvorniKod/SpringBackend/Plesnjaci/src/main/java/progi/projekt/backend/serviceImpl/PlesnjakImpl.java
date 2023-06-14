package progi.projekt.backend.serviceImpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import progi.projekt.backend.model.Dvorana;
import progi.projekt.backend.model.Klub;
import progi.projekt.backend.model.Plesnjak;
import progi.projekt.backend.repository.PlesnjakRepository;
import progi.projekt.backend.service.PlesnjakServis;


@Service
public class PlesnjakImpl implements PlesnjakServis{
	
	PlesnjakRepository plesnjakRepository;
	
	public PlesnjakImpl(@Autowired PlesnjakRepository plesnjakRepository) {
		this.plesnjakRepository = plesnjakRepository;
	}

	@Override
	public Plesnjak dohvatiPlesnjak(Date vrijeme, Long klubId, Long dvoranaId) {
		return plesnjakRepository.dohvatPlesnjak(vrijeme, klubId, dvoranaId);
	}
	
  

	@Override
	public Plesnjak stvoriPlesnjak(Plesnjak plesnjak) {
		Plesnjak dohvaceniPlesnjak = dohvatiPlesnjak(plesnjak.getVrijeme(), 
				plesnjak.getKlubOrganizator().getKlubId(), 
				plesnjak.getDvorana().getDvoranaId());
		if(dohvaceniPlesnjak == null) {
			return plesnjakRepository.save(plesnjak);
		}
		return null;
	}

	@Override
	public Plesnjak updatePlesnjak(Plesnjak updatedPlesnjak, Date vrijeme, Long klubId, Long dvoranaId) {
		Plesnjak stariPlesnjak = dohvatiPlesnjak(vrijeme, klubId, dvoranaId); 
		
		stariPlesnjak.setVrijeme(updatedPlesnjak.getVrijeme());
		stariPlesnjak.setKlubOrganizator(updatedPlesnjak.getKlubOrganizator());
		stariPlesnjak.setNaziv(updatedPlesnjak.getNaziv());
		stariPlesnjak.setDvorana(updatedPlesnjak.getDvorana());
		stariPlesnjak.setOpis(updatedPlesnjak.getOpis());
		stariPlesnjak.setSlika(updatedPlesnjak.getSlika());
		
		return plesnjakRepository.saveAndFlush(stariPlesnjak); 
	}

	@Override
	public Plesnjak obrisiPlesnjakPoId(Long plesnjakId) {
		Optional<Plesnjak> dohvaceniPlesnjak = plesnjakRepository.findById(plesnjakId);
		if(dohvaceniPlesnjak != null) {
			plesnjakRepository.deleteById(dohvaceniPlesnjak.get().getPlesnjakId());
			return dohvaceniPlesnjak.get();
		}
		return null;
	}

	@Override
	public List<Plesnjak> dohvatiSvePlesnjake() {
		return plesnjakRepository.findAll();
	}

	@Override
	public Plesnjak dohvatiPlesnjakById(Long id) {
		return plesnjakRepository.findById(id).get();
	}

	@Override
	public List<Long> dohvatiPlesnjakeFilterKlub(Klub klub) {
		return plesnjakRepository.findByKlubId(klub.getKlubId());
	}
	
	@Override
	public Plesnjak updatePlesnjakId(Plesnjak updatedPlesnjak, Dvorana dvorana) {
	
		Plesnjak stariPlesnjak = dohvatiPlesnjakById(updatedPlesnjak.getPlesnjakId());  
		
		stariPlesnjak.setNaziv(updatedPlesnjak.getNaziv());
		stariPlesnjak.setOpis(updatedPlesnjak.getOpis());
		stariPlesnjak.setSlika(updatedPlesnjak.getSlika());
		stariPlesnjak.setVrijeme(updatedPlesnjak.getVrijeme());
		stariPlesnjak.setDvorana(dvorana);
		
		
		return plesnjakRepository.saveAndFlush(stariPlesnjak); 
		
	}
}
