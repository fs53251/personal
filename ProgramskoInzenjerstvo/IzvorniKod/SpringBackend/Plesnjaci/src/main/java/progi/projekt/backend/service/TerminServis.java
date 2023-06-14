package progi.projekt.backend.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import progi.projekt.backend.model.Termin;
import progi.projekt.backend.model.classes.Tecaj.TecajTermin;

@Service
@Component
public interface TerminServis {
	Termin stvoriTermin(Termin termin);
	Termin obrisiTermin(Long terminId);
	Termin updateTermin(Termin updateTermin);
	Termin dohvatiTerminById(Long terminId);
	List<Termin> dohvatiSveTermine();
	List<Long> dohvatiSveTermineFilterTecaj(long tecajId);
	Termin dohvatiTermin(Date vrijeme, Long dvoranaId);
	Object updateTermini(TecajTermin tecajTermin);
}
