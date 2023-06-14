package progi.projekt.backend.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import progi.projekt.backend.model.UpisTecaj;
import progi.projekt.backend.repository.UpisTecajRepository;
import progi.projekt.backend.service.UpisTecajServis;

@Service
public class UpisTecajImpl implements UpisTecajServis{

	UpisTecajRepository upisTecajRepository;
	
	public UpisTecajImpl(@Autowired UpisTecajRepository upisTecajRepository) {
		this.upisTecajRepository = upisTecajRepository;
	}
	
	@Override
	public UpisTecaj stvoriUpisTecaj(UpisTecaj upisTecaj) {
		if(dohvatiUpisTecaj(upisTecaj.getKorisnik().getKlijentId(), upisTecaj.getTecaj().getTecajId()) != null) {
			return null;
		}
		return upisTecajRepository.save(upisTecaj);
	}

	@Override
	public UpisTecaj dohvatiUpisTecaj(Long klijentId, Long tecajId) {
		UpisTecaj upisTecaj = upisTecajRepository.dohvatiUpisTecaj(klijentId, tecajId);
		return upisTecaj;
	}

	@Override
	public UpisTecaj dohvatiUpisTecajPoId(Long upisTecajId) {
		return upisTecajRepository.findById(upisTecajId).get();
	}

	@Override
	public UpisTecaj potvrdiUpisTecaj(Long upisTecajId) {
		UpisTecaj upisTecaj = dohvatiUpisTecajPoId(upisTecajId);
		upisTecaj.setJePotvrden(true);
		return upisTecajRepository.saveAndFlush(upisTecaj);
	}
	
	public UpisTecaj makniPotvrduUpisTecaj(Long upisTecajId) {
		UpisTecaj upisTecaj = dohvatiUpisTecajPoId(upisTecajId);
		upisTecaj.setJePotvrden(false);
		return upisTecajRepository.saveAndFlush(upisTecaj);
	}
	

	@Override
	public List<UpisTecaj> dohvatiSveUpiseTecaja() {
		return upisTecajRepository.findAll();
	}

	@Override
	public List<UpisTecaj> dohvatiSveNepotvrdeneUpiseTecaja() {
		List<UpisTecaj> upisTecaj = upisTecajRepository.dohvatiSveNepotvrdene();
		return upisTecaj;
	}

	@Override
	public List<UpisTecaj> dohvatiSvePotvrdeneUpiseTecaja() {
		List<UpisTecaj> upisTecaj = upisTecajRepository.dohvatiSvePotvrdene();
		return upisTecaj;
	}

	@Override
	public List<UpisTecaj> dohvatiSveUpiseTecajaFilterTecaj(Long tecajId) {
		List<UpisTecaj> upisTecaj = upisTecajRepository.dohvatiSveFilterTecaj(tecajId);
		return upisTecaj;
	}

	@Override
	public List<UpisTecaj> dohvatiSveNepotvrdeneUpiseTecajaFilterTecaj(Long tecajId) {
		List<UpisTecaj> upisTecaj = upisTecajRepository.dohvatiSveNepotvrdeneFilterTecaj(tecajId);
		return upisTecaj;
	}

	@Override
	public List<UpisTecaj> dohvatiSvePotvrdeneUpiseTecajafilterTecaj(Long tecajId) {
		List<UpisTecaj> upisTecaj = upisTecajRepository.dohvatiSvePotvrdeneFilterTecaj(tecajId);
		return upisTecaj;
	}
	
	@Override
	public List<UpisTecaj> dohvatiSvePotvrdeneUpiseTecajaFilterKlijent(Long klijentId){
		return upisTecajRepository.dohvatiSvePotvrdeneFilterKlijent(klijentId);
	}
}
