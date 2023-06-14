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
import progi.projekt.backend.model.Klub;
import progi.projekt.backend.model.Tecaj;
import progi.projekt.backend.model.Termin;
import progi.projekt.backend.model.TipPlesa;
import progi.projekt.backend.model.UpisTecaj;
import progi.projekt.backend.model.classes.Tecaj.ResponseTecaj;
import progi.projekt.backend.model.classes.Tecaj.TecajTermin;
import progi.projekt.backend.model.classes.Termin.VrijemeDvorana;
import progi.projekt.backend.model.classes.UpisTecaj.ResponseUpisTecaj;
import progi.projekt.backend.serviceImpl.DvoranaImpl;
import progi.projekt.backend.serviceImpl.KlijentImpl;
import progi.projekt.backend.serviceImpl.KlubImpl;
import progi.projekt.backend.serviceImpl.PlesImpl;
import progi.projekt.backend.serviceImpl.TecajImpl;
import progi.projekt.backend.serviceImpl.TerminImpl;
import progi.projekt.backend.serviceImpl.TipPlesaImpl;
import progi.projekt.backend.serviceImpl.UpisTecajImpl;
import progi.projekt.backend.utils.Utils;

@Controller
@RequestMapping("/tecaj")
public class TecajController {
	
	TecajImpl tecajService;
	KlijentImpl klijentService;
	KlubImpl klubService;
	PlesImpl plesService;
	TipPlesaImpl tipPlesaService; 
	DvoranaImpl dvoranaService;
	TerminImpl terminService;
	UpisTecajImpl upisTecajService;
	
	public TecajController(@Autowired TecajImpl tecajService,
						   @Autowired KlijentImpl kljentService,
						   @Autowired KlubImpl klubService,
						   @Autowired PlesImpl plesService,
						   @Autowired TipPlesaImpl tipPlesaService,
						   @Autowired DvoranaImpl dvoranaService,
						   @Autowired TerminImpl terminService,
						   @Autowired UpisTecajImpl upisTecajService) {
		this.tecajService = tecajService;
		this.klijentService = kljentService;
		this.klubService = klubService;
		this.plesService = plesService;
		this.tipPlesaService = tipPlesaService; 
		this.dvoranaService = dvoranaService;
		this.terminService = terminService;
		this.upisTecajService = upisTecajService;
	}
	
	@PostMapping("/add")
	public ResponseEntity<ResponseTecaj> newTecaj(@RequestBody TecajTermin tecajTermin,
			@RequestParam(name = "trenerId") Long trenerId,
			@RequestParam(name = "klubId") Long klubId,
			@RequestParam(name = "plesId") Long plesId){
		Tecaj newTecaj = tecajTermin.getTecaj();
		Klijent trener = klijentService.dohvatiKlijentaPoId(trenerId);
		if(trener == null) {
			ResponseTecaj data = new ResponseTecaj(null, null, true, "Ne postoji korisnik u bazi.");
			return new ResponseEntity<ResponseTecaj>(data, HttpStatus.OK);
		}
		
		Klub klub = klubService.dohvatiKlubPoId(klubId);
		if(klub == null) {
			ResponseTecaj data = new ResponseTecaj(null, null, true, "Ne postoji klub u bazi.");
			return new ResponseEntity<ResponseTecaj>(data, HttpStatus.OK);
		}
		
		TipPlesa ples = plesService.dohvatiPlesPoId(plesId);
		if(ples == null) {
			ResponseTecaj data = new ResponseTecaj(null, null, true, "Ne postoji ples u bazi.");
			return new ResponseEntity<ResponseTecaj>(data, HttpStatus.OK);
		}
		newTecaj.setTrener(trener);
		newTecaj.setKlub(klub);
		newTecaj.setTipPlesa(ples);
		Tecaj tecajFromDB = tecajService.stvoriTecaj(newTecaj);
		
		if(tecajFromDB == null) {
			ResponseTecaj data = new ResponseTecaj(null, null, false, "Neuspješno stvaranje tecaja");
			return new ResponseEntity<ResponseTecaj>(data, HttpStatus.OK);
		}else{
			for(VrijemeDvorana termin : tecajTermin.getTermini()) {
				String adresaDvorane = termin.getAdresaDvorane();
				Dvorana dvorana = dvoranaService.dohvatiDvoranu(adresaDvorane);
				if(dvorana == null) {
					dvorana = dvoranaService.stvoriDvoranu(new Dvorana(adresaDvorane));
				}
				Termin dohvaceniTermin = terminService.dohvatiTermin(termin.getVrijeme(), dvorana.getDvoranaId());
				if(dohvaceniTermin != null) {
					ResponseTecaj data = new ResponseTecaj(null, null, true, String.format("Termin u dvorani %s za vrijeme %s je zauzet", dvorana.getAdresa(), dohvaceniTermin.getVrijeme()));
					return new ResponseEntity<ResponseTecaj>(data, HttpStatus.OK);
				}else {
					terminService.stvoriTermin(new Termin(termin.getVrijeme(), tecajFromDB, dvorana));
				}
			}
			ResponseTecaj data = new ResponseTecaj(tecajFromDB, null, true, "Uspješno stvaranje tecaja");
			return new ResponseEntity<ResponseTecaj>(data, HttpStatus.OK);
		}
	}
	
	@PutMapping("/update")
	public ResponseEntity<ResponseTecaj> updateTecaj(@RequestBody TecajTermin tecajTermin,
			@RequestParam(required = false, name = "trenerId") Long trenerId,
			@RequestParam(required = false, name = "plesId") Long plesId){
		Tecaj updatedTecaj = tecajTermin.getTecaj();
		if(updatedTecaj.getTecajId() == null) {
			ResponseTecaj data = new ResponseTecaj(null, null, true, "Nedostaje tecajId.");
			return new ResponseEntity<ResponseTecaj>(data, HttpStatus.OK);
		}
		Klijent trener = null;
		if(trenerId != null) {
			trener = klijentService.dohvatiKlijentaPoId(trenerId);
			if(trener == null) {
				ResponseTecaj data = new ResponseTecaj(null, null, true, "Ne postoji korisnik u bazi.");
				return new ResponseEntity<ResponseTecaj>(data, HttpStatus.OK);
			}
		}
		TipPlesa ples = null;
		if(plesId != null) {
			ples = plesService.dohvatiPlesPoId(plesId);
			if(ples == null) {
				ResponseTecaj data = new ResponseTecaj(null, null, true, "Ne postoji ples u bazi.");
				return new ResponseEntity<ResponseTecaj>(data, HttpStatus.OK);
			}
		}
		updatedTecaj.setTrener(trener);
		updatedTecaj.setTipPlesa(ples);
		Tecaj tecajFromDB = tecajService.updateTecaj(updatedTecaj);
		if(tecajFromDB == null) {
			ResponseTecaj data = new ResponseTecaj(null, null, false, "Neuspješna izmjena tecaja");
			return new ResponseEntity<ResponseTecaj>(data, HttpStatus.OK);
		}else{
			tecajTermin.setTecaj(tecajFromDB);
			if(!terminService.updateTermini(tecajTermin)) {
				ResponseTecaj data = new ResponseTecaj(null, null, false, "Neuspješna izmjena tecaja, greska izmjene termina tecaja.");
				return new ResponseEntity<ResponseTecaj>(data, HttpStatus.OK);
			}
			ResponseTecaj data = new ResponseTecaj(tecajFromDB, null, true, "Uspješna izmjena tecaja");
			return new ResponseEntity<ResponseTecaj>(data, HttpStatus.OK);
		}
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<ResponseTecaj> deleteTecaj(@RequestParam(name = "tecajId") Long tecajId){
		Tecaj tecaj = tecajService.obrisiTecaj(tecajId);
		if(tecaj == null) {
			ResponseTecaj data = new ResponseTecaj(null, null, false, "Neuspješno brisanje tecaja");
			return new ResponseEntity<ResponseTecaj>(data, HttpStatus.OK);
		}else{
			ResponseTecaj data = new ResponseTecaj(tecaj, null, true, "Uspješno brisanje tecaja");
			return new ResponseEntity<ResponseTecaj>(data, HttpStatus.OK);
		}
	}
	
	@GetMapping("/fetch")
	public ResponseEntity<ResponseTecaj> getTecaj(@RequestParam(name= "tecajId") Long tecajId){
		
		Tecaj tecajFromDB = tecajService.dohvatiTecajById(tecajId); 
		
		if(tecajFromDB == null) {
			ResponseTecaj data = new ResponseTecaj(null, null, false, "Neuspješno dohvaćanje tečaja");
			return new ResponseEntity<ResponseTecaj>(data, HttpStatus.OK);
		}else{
			ResponseTecaj data = new ResponseTecaj(tecajFromDB, null, true, "Uspješno dohvaćanje tečaja");
			return new ResponseEntity<ResponseTecaj>(data, HttpStatus.OK);
		}
		
	}
	
	@GetMapping("/all")
	public ResponseEntity<ResponseTecaj> sviTecajevi(){
		List<Tecaj> tecajevi = tecajService.dohvatiSveTecajeve();
		if(tecajevi == null) {
			ResponseTecaj data = new ResponseTecaj(null, null, false, "Neuspješno dohvaćanje tečaja");
			return new ResponseEntity<ResponseTecaj>(data, HttpStatus.OK);
		}else{
			ResponseTecaj data = new ResponseTecaj(null, tecajevi, true, "Uspješno dohvaćanje tečaja");
			return new ResponseEntity<ResponseTecaj>(data, HttpStatus.OK);
		}
	}
	
	@GetMapping("/filterKlub")
	public ResponseEntity<ResponseTecaj> filtrirajPoKlubu(
			@RequestParam(name = "korisnickoImeVlasnika") String korisnickoImeVlasnikaParam,
			@RequestParam(name = "imeKluba") String imeKlubaParam){
		
		String korisnickoImeVlasnika = Utils.whiteSpaces(korisnickoImeVlasnikaParam); 
		String imeKluba = Utils.whiteSpaces(imeKlubaParam);
		
		Klijent vlasnik = klijentService.dohvatiKlijenta(korisnickoImeVlasnika);
		if(vlasnik == null) {
			ResponseTecaj data = new ResponseTecaj(null, null, true, "Ne postoji korisnik u bazi.");
			return new ResponseEntity<ResponseTecaj>(data, HttpStatus.OK);
		}
		Klub klub = klubService.dohvatiKlub(imeKluba, vlasnik);
		if(klub == null) {
			ResponseTecaj data = new ResponseTecaj(null, null, true, "Ne postoji klub u bazi.");
			return new ResponseEntity<ResponseTecaj>(data, HttpStatus.OK);
		}
		
		List<Long> tecajevi = tecajService.dohvatiTecajeveFilterKlub(klub);
		List<Tecaj> listaTecajeva = new ArrayList<>();
		for(int i = 0; i < tecajevi.size(); i++) {
			Tecaj dohvatiTecaj = tecajService.dohvatiTecajById(tecajevi.get(i));
			if(dohvatiTecaj != null) {
				listaTecajeva.add(dohvatiTecaj);
			}
		}
		ResponseTecaj data = new ResponseTecaj(null, listaTecajeva, true, "Uspješno dohvaćanje tecajeva prema klubu");
		return new ResponseEntity<ResponseTecaj>(data, HttpStatus.OK);
	}
	
	@GetMapping("/filterTipPlesa")
	public ResponseEntity<ResponseTecaj> filtrirajPoTipuPlesa(
			@RequestParam(name = "tipPlesaId") long tipPlesaId){
		
		TipPlesa tipPlesa = tipPlesaService.dohvatiTipPlesaById(tipPlesaId).get(); 
		if(tipPlesa == null) {
			ResponseTecaj data = new ResponseTecaj(null, null, true, "Ne postoji tip plesa u bazi.");
			return new ResponseEntity<ResponseTecaj>(data, HttpStatus.OK);
		}
		
		List<Long> tecajevi = tecajService.dohvatiTecajeveFilterTipPlesaId(tipPlesaId);
		List<Tecaj> listaTecajeva = new ArrayList<>();
		
		for(int i = 0; i < tecajevi.size(); i++) {
			Tecaj dohvatiTecaj = tecajService.dohvatiTecajById(tecajevi.get(i));
			if(dohvatiTecaj != null) {
				listaTecajeva.add(dohvatiTecaj);
			}
		}
		ResponseTecaj data = new ResponseTecaj(null, listaTecajeva, true, "Uspješno dohvaćanje tecajeva prema tipu plesa");
		return new ResponseEntity<ResponseTecaj>(data, HttpStatus.OK);
	}
	
	@GetMapping("/filterTrener")
	public ResponseEntity<ResponseTecaj> filtrirajPoTreneru(
			@RequestParam(name = "trenerId") long trenerId){
		
		Klijent klijent = klijentService.dohvatiKlijentaPoId(trenerId); 
		
		if(klijent == null) {
			ResponseTecaj data = new ResponseTecaj(null, null, true, "Ne postoji klijent u bazi.");
			return new ResponseEntity<ResponseTecaj>(data, HttpStatus.OK);
		}

		List<Long> tecajevi = tecajService.dohvatiTecajeveTrenerId(trenerId);
		List<Tecaj> listaTecajeva = new ArrayList<>();
		
		for(int i = 0; i < tecajevi.size(); i++) {
			Tecaj dohvatiTecaj = tecajService.dohvatiTecajById(tecajevi.get(i));
			if(dohvatiTecaj != null) {
				listaTecajeva.add(dohvatiTecaj);
			}
		}
		ResponseTecaj data = new ResponseTecaj(null, listaTecajeva, true, "Uspješno dohvaćanje tecajeva prema treneru");
		return new ResponseEntity<ResponseTecaj>(data, HttpStatus.OK);
	}
	
	@GetMapping("/upisiTecaj")
	public ResponseEntity<ResponseTecaj> upisiTecaj(@RequestParam(name = "korisnikId") Long korisnikId,
			@RequestParam(name = "tecajId") Long tecajId){
		Klijent klijent = klijentService.dohvatiKlijentaPoId(korisnikId);
		if(klijent == null) {
			ResponseTecaj data = new ResponseTecaj(null, null, true, "Neuspješan upis tečaja, ne postoji klijent u bazi.");
			return new ResponseEntity<ResponseTecaj>(data, HttpStatus.OK);
		}
		Tecaj tecaj = tecajService.dohvatiTecajById(tecajId);
		if(tecaj == null) {
			ResponseTecaj data = new ResponseTecaj(null, null, true, "Neuspješan upis tečaja, ne postoji tečaj u bazi.");
			return new ResponseEntity<ResponseTecaj>(data, HttpStatus.OK);
		}
		UpisTecaj upisTecajFromDB = upisTecajService.stvoriUpisTecaj(new UpisTecaj(klijent, tecaj, false));
		if(upisTecajFromDB == null) {
			ResponseTecaj data = new ResponseTecaj(null, null, false, "Neuspješno upisivanje tečaja");
			return new ResponseEntity<ResponseTecaj>(data, HttpStatus.OK);
		}else{
			ResponseTecaj data = new ResponseTecaj(upisTecajFromDB.getTecaj(), null, true, "Uspješno upisivanje tečaja");
			return new ResponseEntity<ResponseTecaj>(data, HttpStatus.OK);
		}
	}
	
	@GetMapping("/potvrdi")
	public ResponseEntity<ResponseTecaj> potvrdiUpisTecaja(@RequestParam(name = "upisTecajId") Long upisTecajId){
		UpisTecaj upisTecaj = upisTecajService.potvrdiUpisTecaj(upisTecajId);
		if(upisTecaj == null) {
			ResponseTecaj data = new ResponseTecaj(null, null, false, "Neuspješno potvrđivanje upisa tečaja");
			return new ResponseEntity<ResponseTecaj>(data, HttpStatus.OK);
		}else{
			ResponseTecaj data = new ResponseTecaj(upisTecaj.getTecaj(), null, true, "Uspješno potvrđivanje upisa tečaja");
			return new ResponseEntity<ResponseTecaj>(data, HttpStatus.OK);
		}
	}
	
	
	@GetMapping("/makniPotvrdu")
	public ResponseEntity<ResponseTecaj> makniPotvrduUpisTecaja(@RequestParam(name = "upisTecajId") Long upisTecajId){
		UpisTecaj upisTecaj = upisTecajService.makniPotvrduUpisTecaj(upisTecajId);
		if(upisTecaj == null) {
			ResponseTecaj data = new ResponseTecaj(null, null, false, "Neuspješno potvrđivanje upisa tečaja");
			return new ResponseEntity<ResponseTecaj>(data, HttpStatus.OK);
		}else{
			ResponseTecaj data = new ResponseTecaj(upisTecaj.getTecaj(), null, true, "Uspješno potvrđivanje upisa tečaja");
			return new ResponseEntity<ResponseTecaj>(data, HttpStatus.OK);
		}
	}
	
	
	@GetMapping("/allUpisiTecaja")
	public ResponseEntity<ResponseUpisTecaj> dohvatiSveUpiseTecaja(){
		List<UpisTecaj> listaUpisaTecajeva = upisTecajService.dohvatiSveUpiseTecaja();
		if(listaUpisaTecajeva == null) {
			ResponseUpisTecaj data = new ResponseUpisTecaj(null, null, false, "Neuspješan dohvat svih upisa tečajeva");
			return new ResponseEntity<ResponseUpisTecaj>(data, HttpStatus.OK);
		}else {
			ResponseUpisTecaj data = new ResponseUpisTecaj(null, listaUpisaTecajeva, false, "Uspješan dohvat svih upisa tečajeva");
			return new ResponseEntity<ResponseUpisTecaj>(data, HttpStatus.OK);
		}
	}
	
	
	@GetMapping("/all/nepotvrdeni")
	public ResponseEntity<ResponseUpisTecaj> dohvatiSveNepotvrdeneUpiseTecaja(){
		List<UpisTecaj> listaUpisaTecajeva = upisTecajService.dohvatiSveNepotvrdeneUpiseTecaja();
		if(listaUpisaTecajeva == null) {
			ResponseUpisTecaj data = new ResponseUpisTecaj(null, null, false, "Neuspješan dohvat svih nepotvrđenih upisa tečajeva");
			return new ResponseEntity<ResponseUpisTecaj>(data, HttpStatus.OK);
		}else {
			ResponseUpisTecaj data = new ResponseUpisTecaj(null, listaUpisaTecajeva, false, "Uspješan dohvat svih nepotvrđenih upisa tečajeva");
			return new ResponseEntity<ResponseUpisTecaj>(data, HttpStatus.OK);
		}
	}
	
	@GetMapping("/all/potvrdeni")
	public ResponseEntity<ResponseUpisTecaj> dohvatiSvePotvrdeneUpiseTecaja(){
		List<UpisTecaj> listaUpisaTecajeva = upisTecajService.dohvatiSveNepotvrdeneUpiseTecaja();
		if(listaUpisaTecajeva == null) {
			ResponseUpisTecaj data = new ResponseUpisTecaj(null, null, false, "Neuspješan dohvat svih potvrđenih upisa tečajeva");
			return new ResponseEntity<ResponseUpisTecaj>(data, HttpStatus.OK);
		}else {
			ResponseUpisTecaj data = new ResponseUpisTecaj(null, listaUpisaTecajeva, false, "Uspješan dohvat svih potvrđenih upisa tečajeva");
			return new ResponseEntity<ResponseUpisTecaj>(data, HttpStatus.OK);
		}
	}
	
	@GetMapping("/all/filterTecaj")
	public ResponseEntity<ResponseUpisTecaj> dohvatiSveUpiseTecajaFilterTecaj(@RequestParam(name = "tecajId") Long tecajId){
		List<UpisTecaj> listaUpisaTecajeva = upisTecajService.dohvatiSveUpiseTecajaFilterTecaj(tecajId);
		if(listaUpisaTecajeva == null) {
			ResponseUpisTecaj data = new ResponseUpisTecaj(null, null, false, "Neuspješan dohvat svih upisa tečajeva filter tečaj");
			return new ResponseEntity<ResponseUpisTecaj>(data, HttpStatus.OK);
		}else {
			ResponseUpisTecaj data = new ResponseUpisTecaj(null, listaUpisaTecajeva, false, "Uspješan dohvat svih upisa tečajeva filter tečaj");
			return new ResponseEntity<ResponseUpisTecaj>(data, HttpStatus.OK);
		}
	}
	
	@GetMapping("/all/nepotvrdeni/filterTecaj")
	public ResponseEntity<ResponseUpisTecaj> dohvatiSveNepotvrdeneUpiseTecajaFilterTecaj(@RequestParam(name = "tecajId") Long tecajId){
		List<UpisTecaj> listaUpisaTecajeva = upisTecajService.dohvatiSveNepotvrdeneUpiseTecajaFilterTecaj(tecajId);
		if(listaUpisaTecajeva == null) {
			ResponseUpisTecaj data = new ResponseUpisTecaj(null, null, false, "Neuspješan dohvat svih nepotvrđenih upisa tečajeva filter tečaj");
			return new ResponseEntity<ResponseUpisTecaj>(data, HttpStatus.OK);
		}else {
			ResponseUpisTecaj data = new ResponseUpisTecaj(null, listaUpisaTecajeva, false, "Uspješan dohvat svih nepotvrđenih upisa tečajeva filter tečaj");
			return new ResponseEntity<ResponseUpisTecaj>(data, HttpStatus.OK);
		}
	}
	
	@GetMapping("/all/potvrdeni/filterTecaj")
	public ResponseEntity<ResponseUpisTecaj> dohvatiSvePotvrdeneUpiseTecajaFilterTecaj(@RequestParam(name = "tecajId") Long tecajId){
		List<UpisTecaj> listaUpisaTecajeva = upisTecajService.dohvatiSvePotvrdeneUpiseTecajafilterTecaj(tecajId);
		if(listaUpisaTecajeva == null) {
			ResponseUpisTecaj data = new ResponseUpisTecaj(null, null, false, "Neuspješan dohvat svih potvrđenih upisa tečajeva filter tečaj");
			return new ResponseEntity<ResponseUpisTecaj>(data, HttpStatus.OK);
		}else {
			ResponseUpisTecaj data = new ResponseUpisTecaj(null, listaUpisaTecajeva, false, "Uspješan dohvat svih potvrđenih upisa tečajeva filter tečaj");
			return new ResponseEntity<ResponseUpisTecaj>(data, HttpStatus.OK);
		}
	}
}
