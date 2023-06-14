package progi.projekt.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import progi.projekt.backend.model.Dvorana;

@Repository
public interface DvoranaRepository extends JpaRepository<Dvorana, Long>{
	
	@Query("select dvorana from Dvorana dvorana where dvorana.adresa = ?1")
	Dvorana dohvatDvorane(String adresa);
}
