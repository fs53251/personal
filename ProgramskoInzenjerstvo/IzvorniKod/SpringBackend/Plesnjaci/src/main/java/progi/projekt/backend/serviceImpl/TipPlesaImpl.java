package progi.projekt.backend.serviceImpl;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import progi.projekt.backend.model.TipPlesa;
import progi.projekt.backend.repository.TipPlesaRepository;

@Service
public class TipPlesaImpl {
	
	TipPlesaRepository tipPlesaRepository;
	
	public TipPlesaImpl(@Autowired TipPlesaRepository tipPlesaRepository) {
		this.tipPlesaRepository = tipPlesaRepository;
	}
	
	public Optional<TipPlesa> dohvatiTipPlesaById(Long id) {
		//return tipPlesaRepository.findById(id).get();
		return tipPlesaRepository.findById(id); 
	}
	
}
