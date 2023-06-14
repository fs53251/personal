package progi.projekt.backend.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import progi.projekt.backend.model.Klijent;
import progi.projekt.backend.repository.KlijentRepository;
import progi.projekt.backend.service.KlijentServis;

@Service
public class KlijentImpl implements KlijentServis {

	KlijentRepository klijentRepository;

	public KlijentImpl(@Autowired KlijentRepository klijentRepository) {
		this.klijentRepository = klijentRepository;
	}

	@Override
	public Klijent dohvatiKlijentaLozinka(String username, String password) {
		Klijent klijent = klijentRepository.dohvatKlijenta(username);
		if (klijent != null) {
			if (klijent.getLozinka().equals(password)) {
				return klijent;
			}
		}
		return null;
	}
	
	@Override
	public Klijent dohvatiKlijenta(String username) {
		return klijentRepository.dohvatKlijenta(username);
	}
	
	@Override
	public Klijent dohvatiKlijentaPoId(Long klijentId) {
		return klijentRepository.findById(klijentId).get();
	}
	
	@Override
	public Klijent stvoriKlijenta(Klijent novi) {
		Klijent klijent = klijentRepository.dohvatKlijenta(novi.getKorisnicko_ime());
		if(klijent != null) {
			return null;
		}
		novi.setTipKorisnika("Klijent");
		return klijentRepository.save(novi);
	}

	@Override
	public Klijent updateKlijenta(Klijent updatedKlijent) {
		Optional<Klijent> osoba = klijentRepository.findById(updatedKlijent.getKlijentId());
		if (osoba.isPresent()) {
			Klijent klijent = osoba.get();
			String lozinka = klijent.getLozinka();
			updatedKlijent.setLozinka(lozinka);
			return klijentRepository.saveAndFlush(updatedKlijent); 
		}
		return null;
		
	}
	
	@Override
	public Klijent updatePassword(Klijent klijent, String novaLozinka) {
		
		klijent.setLozinka(novaLozinka);
		
		return klijentRepository.saveAndFlush(klijent); 
		
	}

	@Override
	public Klijent obrisiKlijenta(String username) {
		Klijent klijent = klijentRepository.dohvatKlijenta(username);
		
		if (klijent != null) {
			klijentRepository.deleteById(klijent.getKlijentId());
			return klijent;
		}
		return null;
	}

	@Override
	public List<Klijent> dohvatiSveKlijente() {
		List<Klijent> listaKlijenata = klijentRepository.findAll(); 
		if(listaKlijenata != null) {
			return listaKlijenata;
		}
		return null; 
	}

}


