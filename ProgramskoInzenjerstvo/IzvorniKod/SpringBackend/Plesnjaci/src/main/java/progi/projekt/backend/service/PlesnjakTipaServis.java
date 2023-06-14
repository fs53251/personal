package progi.projekt.backend.service;
 
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import progi.projekt.backend.model.Plesnjak;
import progi.projekt.backend.model.PlesnjakTipa;
import progi.projekt.backend.model.TipPlesa;

@Service
@Component
public interface PlesnjakTipaServis {
	PlesnjakTipa spremiPlesnjakPles(Plesnjak plesnjak, TipPlesa ples);
	List<Long> dohvatiPlesnjakeFilterPles(TipPlesa tipPlesa);
	PlesnjakTipa dohvatiPlesnjakTipa(Plesnjak plesnjak, TipPlesa ples);
	List<Long> dohvatiPlesnjakTipaFilterPlesnjak(Plesnjak plesnjak);
	PlesnjakTipa dohvatiPlesnjakTipaPoId(Long id); 
	
}
