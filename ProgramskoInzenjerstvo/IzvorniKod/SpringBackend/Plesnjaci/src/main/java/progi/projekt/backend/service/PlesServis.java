package progi.projekt.backend.service;

import java.util.List;

import progi.projekt.backend.model.TipPlesa;

public interface PlesServis {
	TipPlesa dohvatiPles(String imePlesa);
	TipPlesa stvoriPles(TipPlesa ples);
	TipPlesa updatePles(TipPlesa updatedPles, String imePlesa); 
	TipPlesa obrisiPles(String name);
	List<TipPlesa> dohvatiSvePlesove();
	TipPlesa dohvatiPlesPoId(Long id); 
}
