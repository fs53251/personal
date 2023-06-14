package progi.projekt.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import progi.projekt.backend.model.Tecaj;

public interface TecajRepository extends JpaRepository<Tecaj, Long>{
	
	@Query("select tecaj.tecajId from Tecaj tecaj where tecaj.klub.klubId = ?1")
	List<Long> findByKlubId(Long klubId);
	@Query("select tecaj.tecajId from Tecaj tecaj where tecaj.tipPlesa.tipPlesaId = ?1")
	List<Long> findByTipPlesaId(long tipPlesaId);
	@Query("select tecaj.tecajId from Tecaj tecaj where tecaj.trener.klijentId = ?1")
	List<Long> findByTrenerId(long trenerId);

}
