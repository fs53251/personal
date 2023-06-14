package progi.projekt.backend.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import progi.projekt.backend.model.Plesnjak;
import progi.projekt.backend.model.PlesnjakTipa;
import progi.projekt.backend.model.TipPlesa;
import progi.projekt.backend.repository.PlesnjakTipaRepository;
import progi.projekt.backend.service.PlesnjakTipaServis;
@Service
public class PlesnjakTipaImpl implements PlesnjakTipaServis{
	
	PlesnjakTipaRepository plesnjakTipaRepository;
	
	public PlesnjakTipaImpl(@Autowired PlesnjakTipaRepository plesnjakTipaRepository) {
		this.plesnjakTipaRepository = plesnjakTipaRepository;
	}

	@Override
	public List<Long> dohvatiPlesnjakeFilterPles(TipPlesa tipPlesa) {
		return plesnjakTipaRepository.findByTipPlesa(tipPlesa.getTipPlesaId());
	}
	
	@Override
	public PlesnjakTipa dohvatiPlesnjakTipaPoId(Long id) {
		return plesnjakTipaRepository.findById(id).get(); 
	}
	
	@Override
	public PlesnjakTipa dohvatiPlesnjakTipa(Plesnjak plesnjak, TipPlesa ples) {
		return plesnjakTipaRepository.dohvatPlesnjakTipa(ples.getNaziv(),
				plesnjak.getDvorana().getAdresa(),
				plesnjak.getKlubOrganizator().getImeKluba(),
				plesnjak.getKlubOrganizator().getVlasnik().getKorisnicko_ime(),
				plesnjak.getVrijeme());
	}

	@Override
	public PlesnjakTipa spremiPlesnjakPles(Plesnjak plesnjak, TipPlesa ples) {
		PlesnjakTipa plesnjakTipa = dohvatiPlesnjakTipa(plesnjak, ples);
		if(plesnjakTipa == null) {
			return plesnjakTipaRepository.save(new PlesnjakTipa(plesnjak, ples));
		}
		return null;
	}

	@Override
	public List<Long> dohvatiPlesnjakTipaFilterPlesnjak(Plesnjak plesnjak) {
		return plesnjakTipaRepository.findByPlesnjak(plesnjak.getPlesnjakId());
	}

	public void obisiPlesnjakTipa(long plesnjakTipaId) {
		Optional<PlesnjakTipa> plesnjakTipa = plesnjakTipaRepository.findById(plesnjakTipaId);
		if(plesnjakTipa.isPresent()) {
			plesnjakTipaRepository.deleteById(plesnjakTipaId);
			//return plesnjakTipa.get();
		}
		//return null;
		
	}
	

}
