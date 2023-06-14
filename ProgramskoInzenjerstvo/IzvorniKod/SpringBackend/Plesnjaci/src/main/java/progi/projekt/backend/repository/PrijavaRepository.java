package progi.projekt.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import progi.projekt.backend.model.Prijava;

public interface PrijavaRepository extends JpaRepository<Prijava, Long>{
	
	@Query("select prijava from Prijava prijava where prijava.klijent.klijentId = ?1  and prijava.klub.klubId = ?2")
	Prijava dohvatPrijave(Long trenerId, Long klubId);
	@Query("select prijava from Prijava prijava where prijava.jePotvrden = true and prijava.klub.klubId = ?1")
	List<Prijava> sviPotvrdeniPoKlubu(Long klubId);
	@Query("select prijava from Prijava prijava where prijava.jePotvrden = false and prijava.klub.klubId = ?1")
	List<Prijava> sviNepotvrdeniPoKlubu(Long klubId);
	@Query("select prijava from Prijava prijava where prijava.jePotvrden = true and prijava.klijent.klijentId = ?1")
	List<Prijava> svePotvrdenePoTreneru(Long klijentId);
	
}
