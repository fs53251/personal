package progi.projekt.backend.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import progi.projekt.backend.model.Dvorana;
import progi.projekt.backend.repository.DvoranaRepository;
import progi.projekt.backend.service.DvoranaServis;

@Service
public class DvoranaImpl implements DvoranaServis{
	
	DvoranaRepository dvoranaRepository;
	
	public DvoranaImpl(@Autowired DvoranaRepository dvoranaRepositoy) {
		this.dvoranaRepository = dvoranaRepositoy;
	}

	@Override
	public List<Dvorana> dohvatiDvorane() {
		List<Dvorana> listaDvorana = dvoranaRepository.findAll();
		if(listaDvorana != null) {
			return listaDvorana;
		}
		return null;
	}

	@Override
	public Dvorana stvoriDvoranu(Dvorana dvorana) {
		Dvorana dohvacenaDvorana = dohvatiDvoranu(dvorana.getAdresa());
		if(dohvacenaDvorana == null) {
			return dvoranaRepository.save(dvorana);
		}
		return null;	
	}
	
	@Override
	public Dvorana dohvatiDvoranu(String adresa) {
		return dvoranaRepository.dohvatDvorane(adresa);
	}

	@Override
	public Dvorana obrisiDvoranu(Dvorana dvorana) {
		if(dvoranaRepository.existsById(dvorana.getDvoranaId())) {
			Optional<Dvorana> dvoranaBaza = dvoranaRepository.findById(dvorana.getDvoranaId());
			dvoranaRepository.deleteById(dvorana.getDvoranaId());
			return dvoranaBaza.get();
		}
		return null;
	}

}
