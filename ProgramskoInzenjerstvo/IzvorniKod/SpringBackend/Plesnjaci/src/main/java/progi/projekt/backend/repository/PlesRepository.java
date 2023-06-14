package progi.projekt.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import progi.projekt.backend.model.TipPlesa;

public interface PlesRepository extends JpaRepository<TipPlesa, Long>{
	
	@Query("select tipPlesa from TipPlesa tipPlesa where tipPlesa.naziv = ?1")
	TipPlesa dohvatPles(String imePlesa);
}
