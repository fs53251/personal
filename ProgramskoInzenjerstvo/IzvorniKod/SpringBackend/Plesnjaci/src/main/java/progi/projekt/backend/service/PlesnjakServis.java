package progi.projekt.backend.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import progi.projekt.backend.model.Dvorana;
import progi.projekt.backend.model.Klub;
import progi.projekt.backend.model.Plesnjak;

@Service
@Component
public interface PlesnjakServis {
	Plesnjak dohvatiPlesnjak(Date vrijeme, Long klubId, Long dvoranaId);
	Plesnjak stvoriPlesnjak(Plesnjak plesnjak);
	Plesnjak updatePlesnjak(Plesnjak updatedPlesnjak, Date vrijeme, Long klubId, Long dvoranaId);
	List<Plesnjak> dohvatiSvePlesnjake();
	Plesnjak dohvatiPlesnjakById(Long id);
	List<Long> dohvatiPlesnjakeFilterKlub(Klub klub);
	Plesnjak obrisiPlesnjakPoId(Long plesnjakId);
	Plesnjak updatePlesnjakId(Plesnjak updatedPlesnjak, Dvorana dvorana);
}
