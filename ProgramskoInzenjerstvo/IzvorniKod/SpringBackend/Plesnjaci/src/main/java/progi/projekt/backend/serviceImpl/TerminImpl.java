package progi.projekt.backend.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import progi.projekt.backend.model.Dvorana;
import progi.projekt.backend.model.Termin;
import progi.projekt.backend.model.classes.Tecaj.TecajTermin;
import progi.projekt.backend.model.classes.Termin.VrijemeDvorana;
import progi.projekt.backend.repository.DvoranaRepository;
import progi.projekt.backend.repository.TerminRepository;
import progi.projekt.backend.service.TerminServis;

@Service
public class TerminImpl implements TerminServis{

	TerminRepository terminRepository;
	DvoranaRepository dovranaRepository;
	
	public TerminImpl(@Autowired TerminRepository terminRepository,
						@Autowired DvoranaRepository dvoranaRepository) {
		this.terminRepository = terminRepository;
		this.dovranaRepository = dvoranaRepository;
	}
	
	@Override
	public Termin stvoriTermin(Termin termin) {
		if(dohvatiTermin(termin.getVrijeme(), termin.getDvorana().getDvoranaId()) != null) {
			return null;
		};
		return terminRepository.save(termin);
	}

	@Override
	public Termin obrisiTermin(Long terminId) {
		Optional<Termin> dohvatiTermin = terminRepository.findById(terminId);
		if(dohvatiTermin.isPresent()) {
			terminRepository.deleteById(terminId);
			return dohvatiTermin.get();
		}
		return null;
	}

	@Override
	public Termin updateTermin(Termin updateTermin) {
		Termin dohvatiTermin = dohvatiTerminById(updateTermin.getTerminId());
		dohvatiTermin.setVrijeme(updateTermin.getVrijeme());
		dohvatiTermin.setTecaj(updateTermin.getTecaj());
		dohvatiTermin.setDvorana(updateTermin.getDvorana());
		
		return terminRepository.saveAndFlush(updateTermin);
	}
	
	@Override
	public Termin dohvatiTermin(Date vrijeme, Long dvoranaId) {
		Termin termin = terminRepository.dohvatiTermin(vrijeme, dvoranaId);
		return termin;
	}

	@Override
	public Termin dohvatiTerminById(Long terminId) {
		return terminRepository.findById(terminId).get();
	}

	@Override
	public List<Termin> dohvatiSveTermine() {
		return terminRepository.findAll();
	}

	@Override
	public List<Long> dohvatiSveTermineFilterTecaj(long tecajId) {
		return terminRepository.findAllFilterTecaj(tecajId);
	}

	@Override
	public Boolean updateTermini(TecajTermin tecajTermin) {
		List<Termin> terminiFromDB = terminRepository.findAllFilterTecajReturnEntity(tecajTermin.getTecaj().getTecajId());
		List<VrijemeDvorana> terminiNew = tecajTermin.getTermini();
		
		List<Termin> novi = new ArrayList<>();
		for(VrijemeDvorana vrijemeDvorana : terminiNew) {
			String novaAdresa = vrijemeDvorana.getAdresaDvorane();
			Date novoVrijeme = vrijemeDvorana.getVrijeme();
			boolean izmjeni = false;
			for(Termin termin : terminiFromDB) {
				if(termin.getDvorana().getAdresa().equals(novaAdresa) && termin.getVrijeme().equals(novoVrijeme)) {
					novi.add(termin);
					izmjeni = true;
				}
			}
			if(!izmjeni) {
				Dvorana dohvacenaDvorana = dovranaRepository.dohvatDvorane(novaAdresa);
				if(dohvacenaDvorana == null) {
					dohvacenaDvorana = dovranaRepository.save(new Dvorana(novaAdresa));
				}
				novi.add(terminRepository.save(new Termin(novoVrijeme, tecajTermin.getTecaj(), dohvacenaDvorana))); 
			}
			
		}
		for(Termin stari : terminiFromDB) {
			if(!novi.contains(stari)) {
				terminRepository.deleteById(stari.getTerminId());
			}
		}
		return true;
	}
}
