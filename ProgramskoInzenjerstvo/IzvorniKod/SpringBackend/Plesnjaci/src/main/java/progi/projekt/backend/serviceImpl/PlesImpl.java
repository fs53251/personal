package progi.projekt.backend.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import progi.projekt.backend.model.TipPlesa;
import progi.projekt.backend.repository.PlesRepository;
import progi.projekt.backend.service.PlesServis;

@Service
public class PlesImpl implements PlesServis{
	
	PlesRepository plesRepository;
	
	public PlesImpl(@Autowired PlesRepository plesRepository) {
		this.plesRepository = plesRepository;
	}

	@Override
	public TipPlesa dohvatiPles(String imePlesa) {
		return plesRepository.dohvatPles(imePlesa);
	}
	
	@Override
	public TipPlesa dohvatiPlesPoId(Long id) {
		return plesRepository.findById(id).get();
	}

	@Override
	public TipPlesa stvoriPles(TipPlesa ples) {
		TipPlesa dohvaceniPles = dohvatiPles(ples.getNaziv());
		if(dohvaceniPles == null) {
			return plesRepository.save(ples);
		}
		return null;
	}

	@Override
	public TipPlesa updatePles(TipPlesa updatedPles, String imePlesa) {
		TipPlesa stariPles = dohvatiPles(imePlesa);
		stariPles.setLink(updatedPles.getLink());
		stariPles.setOpis(updatedPles.getOpis());
		stariPles.setSlika(updatedPles.getSlika());
		
		return plesRepository.saveAndFlush(stariPles); 
	}

	@Override
	public TipPlesa obrisiPles(String nazivPlesa) {
		TipPlesa dohvaceniPles = dohvatiPles(nazivPlesa);
		if(dohvaceniPles != null) {
			plesRepository.deleteById(dohvaceniPles.getTipPlesaId());
			return dohvaceniPles;
		}
		return null;
	}

	@Override
	public List<TipPlesa> dohvatiSvePlesove() {
		List<TipPlesa> listaPlesova = plesRepository.findAll(); 
		if(listaPlesova != null) {
			return listaPlesova;
		}
		return null; 
	}

	
}
