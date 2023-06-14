package progi.projekt.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import progi.projekt.backend.model.Klijent;

public interface KlijentRepository extends JpaRepository<Klijent, Long>{
	
	@Query("select klijent from Klijent klijent where klijent.korisnicko_ime = ?1")
	Klijent dohvatKlijenta(String korisnickoIme);
}
