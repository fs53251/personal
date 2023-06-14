package progi.projekt.backend.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import progi.projekt.backend.model.Klub;
import progi.projekt.backend.model.Tecaj;
import progi.projekt.backend.repository.TecajRepository;
import progi.projekt.backend.service.TecajServis;

@Service
public class TecajImpl implements TecajServis{
	
	TecajRepository tecajRepository;
	
	public TecajImpl(@Autowired TecajRepository tecajRepository) {
		this.tecajRepository = tecajRepository;
	}
	
	@Override
	public Tecaj stvoriTecaj(Tecaj tecaj) {
		return tecajRepository.save(tecaj);	
	}
	
	@Override
	public Tecaj obrisiTecaj(Long tecajId) {
		Optional<Tecaj> tecaj = tecajRepository.findById(tecajId);
		if(tecaj.isPresent()) {
			tecajRepository.deleteById(tecajId);
			return tecaj.get();
		}
		return null;
	}
	
	@Override
	public Tecaj updateTecaj(Tecaj updateTecaj) {
		return tecajRepository.saveAndFlush(updateTecaj);
	}

	@Override
	public Tecaj dohvatiTecajById(Long tecajId) {
		return tecajRepository.findById(tecajId).get();
	}

	@Override
	public List<Long> dohvatiTecajeveFilterKlub(Klub klub) {
		return tecajRepository.findByKlubId(klub.getKlubId());
	}

	@Override
	public List<Long> dohvatiTecajeveFilterTipPlesaId(long tipPlesaId) {
		return tecajRepository.findByTipPlesaId(tipPlesaId);
	}

	@Override
	public List<Long> dohvatiTecajeveTrenerId(long trenerId) {
		return tecajRepository.findByTrenerId(trenerId);
	}
	
	@Override
	public List<Tecaj> dohvatiSveTecajeve(){
		return tecajRepository.findAll();
	}
}
