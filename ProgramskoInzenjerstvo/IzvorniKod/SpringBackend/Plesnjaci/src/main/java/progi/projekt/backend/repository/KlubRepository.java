package progi.projekt.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import progi.projekt.backend.model.Klub;

public interface KlubRepository extends JpaRepository<Klub, Long>{
	
	@Query("select klub from Klub klub where klub.imeKluba = ?1  and klub.vlasnik.korisnicko_ime = ?2")
	Klub dohvatKlub(String imeKluba, String korisnickoImeVlasnika);
	
	@Query("select klub from Klub klub where klub.jePotvrden = true")
    List<Klub> sviPotvrdeni();
	
	@Query("select klub from Klub klub where klub.jePotvrden = false")
    List<Klub> sviNepotvrdeni();
	
	@Query("select klub from Klub klub where klub.vlasnik.klijentId = ?1")
	List<Klub> filterByVlasnik(Long vlasnikId);
	
}
