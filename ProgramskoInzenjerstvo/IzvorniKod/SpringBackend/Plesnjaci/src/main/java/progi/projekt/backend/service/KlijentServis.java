package progi.projekt.backend.service;


import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import progi.projekt.backend.model.Klijent;

@Service
@Component
public interface KlijentServis {
	Klijent dohvatiKlijentaLozinka(String username, String password);
	
	Klijent dohvatiKlijenta(String username);

	Klijent stvoriKlijenta(Klijent novi);

	Klijent updateKlijenta(Klijent updatedklijent);

	Klijent obrisiKlijenta(String username);

	Klijent updatePassword(Klijent stariPasswordKlijent, String novaLozinka);
	
	List<Klijent> dohvatiSveKlijente();

	Klijent dohvatiKlijentaPoId(Long klijentId);
}
