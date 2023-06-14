package progi.projekt.backend.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import progi.projekt.backend.model.Plesnjak;

public interface PlesnjakRepository extends JpaRepository<Plesnjak, Long>{

	@Query("select plesnjak from Plesnjak plesnjak where plesnjak.vrijeme = ?1 "
			+ "and plesnjak.klubOrganizator.klubId = ?2 "
			+ "and plesnjak.dvorana.dvoranaId = ?3")
	Plesnjak dohvatPlesnjak(Date vrijeme, Long klubId, Long dvoranaId);
	
	@Query("select plesnjak.plesnjakId from Plesnjak plesnjak where plesnjak.klubOrganizator.klubId = ?1")
	List<Long> findByKlubId(Long klubId);
}
