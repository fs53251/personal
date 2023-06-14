package progi.projekt.backend.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import progi.projekt.backend.model.Klub;
import progi.projekt.backend.model.Tecaj;

@Service
@Component
public interface TecajServis {
	Tecaj stvoriTecaj(Tecaj tecaj);

	Tecaj obrisiTecaj(Long tecajId);

	Tecaj updateTecaj(Tecaj updateTecaj);

	Tecaj dohvatiTecajById(Long tecajId);

	List<Long> dohvatiTecajeveFilterKlub(Klub klub);

	List<Long> dohvatiTecajeveFilterTipPlesaId(long tipPlesaId);

	List<Long> dohvatiTecajeveTrenerId(long trenerId);

	List<Tecaj> dohvatiSveTecajeve();
}
