package progi.projekt.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import progi.projekt.backend.model.UpisTecaj;

public interface UpisTecajRepository extends JpaRepository<UpisTecaj, Long>{

	@Query("select upisTecaj from UpisTecaj upisTecaj where korisnik.klijentId = ?1 and tecaj.tecajId = ?2")
	UpisTecaj dohvatiUpisTecaj(Long klijentId, Long tecajId);

	@Query("select upisTecaj from UpisTecaj upisTecaj where upisTecaj.jePotvrden = false")
	List<UpisTecaj> dohvatiSveNepotvrdene();
	
	@Query("select upisTecaj from UpisTecaj upisTecaj where upisTecaj.jePotvrden = true")
	List<UpisTecaj> dohvatiSvePotvrdene();

	@Query("select upisTecaj from UpisTecaj upisTecaj where tecaj.tecajId = ?1")
	List<UpisTecaj> dohvatiSveFilterTecaj(Long tecajId);

	@Query("select upisTecaj from UpisTecaj upisTecaj where tecaj.tecajId = ?1 and upisTecaj.jePotvrden = false")
	List<UpisTecaj> dohvatiSveNepotvrdeneFilterTecaj(Long tecajId);

	@Query("select upisTecaj from UpisTecaj upisTecaj where tecaj.tecajId = ?1 and upisTecaj.jePotvrden = true")
	List<UpisTecaj> dohvatiSvePotvrdeneFilterTecaj(Long tecajId);

	@Query("select upisTecaj from UpisTecaj upisTecaj where upisTecaj.jePotvrden = true and upisTecaj.korisnik.klijentId = ?1")
	List<UpisTecaj> dohvatiSvePotvrdeneFilterKlijent(Long klijentId);

}
