package progi.projekt.backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import progi.projekt.backend.model.Dvorana;
import progi.projekt.backend.model.Klijent;
import progi.projekt.backend.model.Tecaj;
import progi.projekt.backend.model.Termin;
import progi.projekt.backend.model.UpisTecaj;
import progi.projekt.backend.model.classes.Termin.ResponseTermin;
import progi.projekt.backend.model.classes.Termin.TerminiKlijentTrener;
import progi.projekt.backend.serviceImpl.DvoranaImpl;
import progi.projekt.backend.serviceImpl.KlijentImpl;
import progi.projekt.backend.serviceImpl.TecajImpl;
import progi.projekt.backend.serviceImpl.TerminImpl;
import progi.projekt.backend.serviceImpl.UpisTecajImpl;

@Controller
@RequestMapping("/termin")
public class TerminController {
	
	TerminImpl terminService;
	DvoranaImpl dvoranaService;
	TecajImpl tecajService;
	KlijentImpl klijentService;
	UpisTecajImpl upisTecajService;
	
	
	public TerminController(@Autowired TerminImpl terminService,
							@Autowired DvoranaImpl dovranaService,
							@Autowired TecajImpl tecajService,
							@Autowired KlijentImpl klijentService,
							@Autowired UpisTecajImpl upisTecajService) {
		this.terminService = terminService;
		this.dvoranaService = dovranaService;
		this.tecajService = tecajService;
		this.klijentService = klijentService;
		this.upisTecajService = upisTecajService;
	}
	
	@PostMapping("/add")
	public ResponseEntity<ResponseTermin> newTermin(@RequestBody Termin termin,
			@RequestParam(name = "tecajId") Long tecajId,
			@RequestParam(name = "adresa") String adresa){
		Dvorana dvorana = dvoranaService.dohvatiDvoranu(adresa);
		if(dvorana == null) {
			dvorana = dvoranaService.stvoriDvoranu(new Dvorana(adresa));
		}
		Tecaj tecaj = null;
		try {
			tecaj = tecajService.dohvatiTecajById(tecajId);
		}catch(Exception e) {
			ResponseTermin data = new ResponseTermin(null, null, false, "Neuspješno dodavanje termina");
			return new ResponseEntity<ResponseTermin>(data, HttpStatus.OK);
		}
		termin.setTecaj(tecaj);
		termin.setDvorana(dvorana);
		
		Termin terminFromDB = terminService.stvoriTermin(termin);
		if(terminFromDB == null) {
			ResponseTermin data = new ResponseTermin(null, null, false, "Neuspješno dodavanje termina");
			return new ResponseEntity<ResponseTermin>(data, HttpStatus.OK);
		}else{
			ResponseTermin data = new ResponseTermin(terminFromDB, null, true, "Uspješno dodavanje termina");
			return new ResponseEntity<ResponseTermin>(data, HttpStatus.OK);
		}
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<ResponseTermin> deleteTermin(@RequestParam(name = "terminId") Long terminId){
		Termin termin = terminService.obrisiTermin(terminId);
		if(termin == null) {
			ResponseTermin data = new ResponseTermin(null, null, false, "Neuspješno brisanje termina");
			return new ResponseEntity<ResponseTermin>(data, HttpStatus.OK);
		}else{
			Tecaj tecaj = termin.getTecaj();
			if(tecaj.getTermini().size() == 0) {
				tecajService.obrisiTecaj(tecaj.getTecajId());
			}
			ResponseTermin data = new ResponseTermin(termin, null, true, "Uspješno brisanje termina");
			return new ResponseEntity<ResponseTermin>(data, HttpStatus.OK);
		}
	}
	
	@PutMapping("/update")
	public ResponseEntity<ResponseTermin> updateTermin(@RequestBody Termin termin,
			@RequestParam(required = false, name = "tecajId") Long tecajId,
			@RequestParam(required = false, name = "adresa") String adresa){
		if(adresa != null) {
			Dvorana dvorana = dvoranaService.dohvatiDvoranu(adresa);
			if(dvorana == null) {
				dvorana = dvoranaService.stvoriDvoranu(new Dvorana(adresa));
			}
			termin.setDvorana(dvorana);
		}
		
		
		if(tecajId != null) {
			Tecaj tecaj = tecajService.dohvatiTecajById(tecajId);
			termin.setTecaj(tecaj);
		}
	
		Termin terminFromDB = terminService.updateTermin(termin);
		if(terminFromDB == null) {
			ResponseTermin data = new ResponseTermin(null, null, false, "Neuspješna izmjena termina");
			return new ResponseEntity<ResponseTermin>(data, HttpStatus.OK);
		}else{
			ResponseTermin data = new ResponseTermin(terminFromDB, null, true, "Uspješna izmjena termina");
			return new ResponseEntity<ResponseTermin>(data, HttpStatus.OK);
		}
	}
	
	@GetMapping("/fetch")
	public ResponseEntity<ResponseTermin> dohvatiTermin(@RequestParam(name = "terminId") Long terminId){
		Termin terminFromDB  = terminService.dohvatiTerminById(terminId);
		if(terminFromDB == null) {
			ResponseTermin data = new ResponseTermin(null, null, false, "Neuspješno dohvacanje termina");
			return new ResponseEntity<ResponseTermin>(data, HttpStatus.OK);
		}else{
			ResponseTermin data = new ResponseTermin(terminFromDB, null, true, "Uspješno dohvacanje termina");
			return new ResponseEntity<ResponseTermin>(data, HttpStatus.OK);
		}
	}
	
	@GetMapping("/all")
	public ResponseEntity<ResponseTermin> updateTermin(){
		List<Termin> listaTermina = terminService.dohvatiSveTermine();
		if(listaTermina == null) {
			ResponseTermin data = new ResponseTermin(null, null, false, "Neuspješna izmjena termina");
			return new ResponseEntity<ResponseTermin>(data, HttpStatus.OK);
		}else{
			ResponseTermin data = new ResponseTermin(null, listaTermina, true, "Uspješna izmjena termina");
			return new ResponseEntity<ResponseTermin>(data, HttpStatus.OK);
		}
	}
	
	@GetMapping("/filterTecaj")
	public ResponseEntity<ResponseTermin> dohvatiTermineFilterTecaj(@RequestParam(name = "tecajId") Long tecajId){
		List<Long> terminFromDB  = terminService.dohvatiSveTermineFilterTecaj(tecajId);
		List<Termin> listaTermina = new ArrayList<>();
		for(int i = 0; i < terminFromDB.size(); i++) {
			Termin termin = terminService.dohvatiTerminById(terminFromDB.get(i));
			listaTermina.add(termin);
		}
		ResponseTermin data = new ResponseTermin(null, listaTermina, true, "Uspješno dohvacanje termina");
		return new ResponseEntity<ResponseTermin>(data, HttpStatus.OK);
	}
	
	@GetMapping("/terminiTrenerKlijent")
	public ResponseEntity<TerminiKlijentTrener> dohvatiTermineKlijentTrener(@RequestParam(name = "username") String username){
		Klijent klijent = klijentService.dohvatiKlijenta(username);
		if(klijent == null) {
			TerminiKlijentTrener data = new TerminiKlijentTrener(null, null, true, "Uspješno dohvacanje termina");
			return new ResponseEntity<TerminiKlijentTrener>(data, HttpStatus.OK);
		}
		
		List<Termin> klijentTermini = new ArrayList<>();
		List<Termin> trenerTermini = new ArrayList<>();
		
		//popuni kalendar upisa tecajeva za klijenta
		List<UpisTecaj> upisaniTecajeviKlijent = upisTecajService.dohvatiSvePotvrdeneUpiseTecajaFilterKlijent(klijent.getKlijentId());
		if(upisaniTecajeviKlijent.size() > 0) {
			for(UpisTecaj upisTecaj : upisaniTecajeviKlijent) {
				Long id = upisTecaj.getTecaj().getTecajId();
				List<Long> termini = terminService.dohvatiSveTermineFilterTecaj(id);
				if(termini.size() > 0) {
					for(Long l : termini) {
						Termin termin = terminService.dohvatiTerminById(l);
						if(termin != null) {
							klijentTermini.add(termin);
						}
					}
				}
			}
		}
		
		//popuni kalendar trenera
		List<Long> tecajeviKojeVodiTrener = tecajService.dohvatiTecajeveTrenerId(klijent.getKlijentId());
		if(tecajeviKojeVodiTrener.size() > 0) {
			for(Long tecajId : tecajeviKojeVodiTrener) {
				List<Long> lista = terminService.dohvatiSveTermineFilterTecaj(tecajId);
				if(lista.size() > 0) {
					for(Long terminId : lista) {
						Termin termin = terminService.dohvatiTerminById(terminId);
						if(termin != null) {
							trenerTermini.add(termin);
						}
					}
				}
			}
		}
		
		TerminiKlijentTrener data = new TerminiKlijentTrener(trenerTermini, klijentTermini, true, "Uspješno dohvacanje termina za trenera i klijenta");
		return new ResponseEntity<TerminiKlijentTrener>(data, HttpStatus.OK);
	}
}
