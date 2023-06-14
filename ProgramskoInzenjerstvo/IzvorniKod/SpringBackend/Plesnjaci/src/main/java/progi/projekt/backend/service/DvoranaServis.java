package progi.projekt.backend.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import progi.projekt.backend.model.Dvorana;

@Service
@Component
public interface DvoranaServis {
	Dvorana dohvatiDvoranu(String adresa);
	List<Dvorana> dohvatiDvorane();
	Dvorana stvoriDvoranu(Dvorana dvorana);
	Dvorana obrisiDvoranu(Dvorana dvorana);
}
