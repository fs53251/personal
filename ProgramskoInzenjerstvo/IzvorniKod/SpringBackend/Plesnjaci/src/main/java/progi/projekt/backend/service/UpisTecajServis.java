package progi.projekt.backend.service;

import java.util.List;

import progi.projekt.backend.model.UpisTecaj;

public interface UpisTecajServis {
	UpisTecaj stvoriUpisTecaj(UpisTecaj upisTecaj);

	UpisTecaj dohvatiUpisTecaj(Long klijentId, Long tecajId);
	
	UpisTecaj dohvatiUpisTecajPoId(Long upisTecajId);

	UpisTecaj potvrdiUpisTecaj(Long upisTecajId);

	//admin
	List<UpisTecaj> dohvatiSveUpiseTecaja();
	
	List<UpisTecaj> dohvatiSveNepotvrdeneUpiseTecaja();
	
	List<UpisTecaj> dohvatiSvePotvrdeneUpiseTecaja();
	
	//vlasnik kluba
	List<UpisTecaj> dohvatiSveUpiseTecajaFilterTecaj(Long tecajId);
	
	List<UpisTecaj> dohvatiSveNepotvrdeneUpiseTecajaFilterTecaj(Long tecajId);

	List<UpisTecaj> dohvatiSvePotvrdeneUpiseTecajafilterTecaj(Long tecajId);

	List<UpisTecaj> dohvatiSvePotvrdeneUpiseTecajaFilterKlijent(Long klijentId);
}
