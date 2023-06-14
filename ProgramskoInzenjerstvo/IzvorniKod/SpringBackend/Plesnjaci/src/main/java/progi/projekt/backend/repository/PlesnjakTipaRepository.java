package progi.projekt.backend.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import progi.projekt.backend.model.PlesnjakTipa;

public interface PlesnjakTipaRepository extends JpaRepository<PlesnjakTipa, Long>{
	
	@Query("select plesnjakTipa from PlesnjakTipa plesnjakTipa where plesnjakTipa.tipPlesa.naziv = ?1 "
			+ "and plesnjakTipa.plesnjak.dvorana.adresa = ?2 "
			+ "and plesnjakTipa.plesnjak.klubOrganizator.imeKluba = ?3 "
			+ "and plesnjakTipa.plesnjak.klubOrganizator.vlasnik.korisnicko_ime = ?4 "
			+ "and plesnjakTipa.plesnjak.vrijeme = ?5")
	PlesnjakTipa dohvatPlesnjakTipa(String tipPlesaNaziv, 
			String plesnjakDvoranaAdresa,
			String plesnjakKlubOrganizatorImeKluba,
			String plesnjakKlubOrganizatorVlasnikKorisnickoIme,
			Date vrijeme);
	
	@Query("select plesnjakTipa.plesnjak.plesnjakId from PlesnjakTipa plesnjakTipa where plesnjakTipa.tipPlesa.tipPlesaId = ?1")
	List<Long> findByTipPlesa(Long tipPlesaId);
	@Query("select plesnjakTipa.tipPlesa.tipPlesaId from PlesnjakTipa plesnjakTipa where plesnjakTipa.plesnjak.plesnjakId = ?1")
	List<Long> findByPlesnjak(Long plesnjakId); 

}
