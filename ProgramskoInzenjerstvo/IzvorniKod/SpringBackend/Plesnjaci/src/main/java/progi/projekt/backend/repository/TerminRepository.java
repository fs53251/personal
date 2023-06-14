package progi.projekt.backend.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import progi.projekt.backend.model.Termin;

public interface TerminRepository extends JpaRepository<Termin, Long>{

	@Query("select termin.terminId from Termin termin where termin.tecaj.tecajId = ?1")
	List<Long> findAllFilterTecaj(long tecajId);

	@Query("select termin from Termin termin where termin.vrijeme = ?1 and termin.dvorana.dvoranaId = ?2")
	Termin dohvatiTermin(Date vrijeme, Long dvoranaId);

	@Query("select termin from Termin termin where termin.tecaj.tecajId = ?1")
	List<Termin> findAllFilterTecajReturnEntity(Long tecajId);

}
