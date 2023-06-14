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
import progi.projekt.backend.model.Plesnjak;
import progi.projekt.backend.model.PlesnjakTipa;
import progi.projekt.backend.model.Prijava;
import progi.projekt.backend.model.Tecaj;
import progi.projekt.backend.model.TipPlesa;
import progi.projekt.backend.model.classes.Klijent.ResponseKlijent;
import progi.projekt.backend.model.classes.Klub.ResponseKlub;
import progi.projekt.backend.model.classes.Prijava.ResponsePrijava;
import progi.projekt.backend.serviceImpl.DvoranaImpl;
import progi.projekt.backend.serviceImpl.KlijentImpl;
import progi.projekt.backend.serviceImpl.KlubImpl;
import progi.projekt.backend.serviceImpl.PlesnjakImpl;
import progi.projekt.backend.serviceImpl.PlesnjakTipaImpl;
import progi.projekt.backend.serviceImpl.PrijavaImpl;
import progi.projekt.backend.serviceImpl.TecajImpl;
import progi.projekt.backend.serviceImpl.TipPlesaImpl;
import progi.projekt.backend.utils.*;

@Controller
@RequestMapping("/klub")
public class KlubController {
	
	KlubImpl klubService;
	KlijentImpl klijentService;
	DvoranaImpl dvoranaService;
	PlesnjakImpl plesnjakService; 
	PlesnjakTipaImpl plesnjakTipaService;
	TipPlesaImpl tipPlesaService; 
	TecajImpl tecajService;
	PrijavaImpl prijavaService; 
	
	public KlubController(@Autowired KlubImpl klubService,
			@Autowired KlijentImpl klijentService,
			@Autowired DvoranaImpl dvoranaService,
			@Autowired PlesnjakImpl plesnjakService,
			@Autowired PlesnjakTipaImpl plesnjakTipaService,
			@Autowired TipPlesaImpl tipPlesaService,
			@Autowired TecajImpl tecajService,
			@Autowired PrijavaImpl prijavaService) {
		this.klubService = klubService;
		this.klijentService = klijentService;
		this.dvoranaService = dvoranaService;
		this.plesnjakService = plesnjakService; 
		this.plesnjakTipaService = plesnjakTipaService;
		this.tipPlesaService = tipPlesaService; 
		this.tecajService = tecajService; 
		this.prijavaService = prijavaService; 
	}
	
	@GetMapping("/all")
	public ResponseEntity<ResponseKlub> getAllKlubovi(){
		
		List<Klub> listaKlubova = klubService.dohvatiSveKlubove(); 
		
		ResponseKlub data = new ResponseKlub(null, listaKlubova, true, "Dohvacanje svih klubova uspjesno!");
		return new ResponseEntity<ResponseKlub>(data, HttpStatus.OK); 
		
	}
	
	@GetMapping("/fetch")
	public ResponseEntity<ResponseKlub> getKlub(@RequestParam(name = "korisnickoImeVlasnika") String korisnickoImeParam, @RequestParam(name = "imeKluba") String imeKlubaParam){
		
		String korisnickoIme = Utils.whiteSpaces(korisnickoImeParam);
		String imeKluba = Utils.whiteSpaces(imeKlubaParam); 
		
		Klijent vlasnik = klijentService.dohvatiKlijenta(korisnickoIme);
		
		if(vlasnik == null) {
			ResponseKlub data = new ResponseKlub(null, null, true, "Ne postoji korisnik u bazi.");
			return new ResponseEntity<ResponseKlub>(data, HttpStatus.OK); 
		}
		
		Klub klub = klubService.dohvatiKlub(imeKluba, vlasnik);
		
		if(klub == null) {
			ResponseKlub data = new ResponseKlub(null, null, true, "Ne postoji klub u bazi.");
			return new ResponseEntity<ResponseKlub>(data, HttpStatus.OK); 
		}
		
		ResponseKlub data = new ResponseKlub(klub, null, true, "Uspjesno dohvacanje kluba");
		return new ResponseEntity<ResponseKlub>(data, HttpStatus.OK); 
		
	}
	
	@GetMapping("/fetch-prijava")
	public ResponseEntity<ResponsePrijava> getPrijava(@RequestParam(name = "korisnickoImeTrenera") String korisnickoImeTreneraParam,
			@RequestParam(name = "korisnickoImeVlasnika") String korisnickoImeVlasnikaParam,
			@RequestParam(name = "imeKluba") String imeKlubaParam){
		
		String korisnickoImeTrenera = Utils.whiteSpaces(korisnickoImeTreneraParam);
		String korisnickoImeVlasnika = Utils.whiteSpaces(korisnickoImeVlasnikaParam);
		String imeKluba = Utils.whiteSpaces(imeKlubaParam);
		
		Klijent trener = klijentService.dohvatiKlijenta(korisnickoImeTrenera);
		if(trener == null) {
			ResponsePrijava data = new ResponsePrijava(null, null, true, "Ne postoji korisnik trener u bazi.");
			return new ResponseEntity<ResponsePrijava>(data, HttpStatus.OK); 
		}
		
		Klijent vlasnik = klijentService.dohvatiKlijenta(korisnickoImeVlasnika);
		if(vlasnik == null) {
			ResponsePrijava data = new ResponsePrijava(null, null, true, "Ne postoji korisnik vlasnik u bazi.");
			return new ResponseEntity<ResponsePrijava>(data, HttpStatus.OK); 
		}
		
		Klub klub = klubService.dohvatiKlub(imeKluba, vlasnik);
		
		if(klub == null) {
			ResponsePrijava data = new ResponsePrijava(null, null, true, "Ne postoji klub u bazi.");
			return new ResponseEntity<ResponsePrijava>(data, HttpStatus.OK); 
		}
		
		Prijava prijava = prijavaService.dohvatiPrijavu(trener, klub); 
		
		if(prijava == null) {
			ResponsePrijava data = new ResponsePrijava(null, null, true, "Ne postoji prijava u bazi.");
			return new ResponseEntity<ResponsePrijava>(data, HttpStatus.OK); 
		}
		
		ResponsePrijava data = new ResponsePrijava(prijava, null, true, "Uspjesno dohvacanje prijave!");
		return new ResponseEntity<ResponsePrijava>(data, HttpStatus.OK); 
		
		
	}
	
	@DeleteMapping("/odbij-prijavu")
	public ResponseEntity<ResponsePrijava> deletePrijava(@RequestParam(name = "korisnickoImeTrenera") String korisnickoImeTreneraParam,
			@RequestParam(name = "korisnickoImeVlasnika") String korisnickoImeVlasnikaParam,
			@RequestParam(name = "imeKluba") String imeKlubaParam){
				
			// dohvacanje trenera i kluba
			String korisnickoImeTrenera = Utils.whiteSpaces(korisnickoImeTreneraParam);
			String korisnickoImeVlasnika = Utils.whiteSpaces(korisnickoImeVlasnikaParam);
			String imeKluba = Utils.whiteSpaces(imeKlubaParam);
			
			Klijent trener = klijentService.dohvatiKlijenta(korisnickoImeTrenera);
			if(trener == null) {
				ResponsePrijava data = new ResponsePrijava(null, null, true, "Ne postoji korisnik trener u bazi.");
				return new ResponseEntity<ResponsePrijava>(data, HttpStatus.OK); 
			}
			
			Klijent vlasnik = klijentService.dohvatiKlijenta(korisnickoImeVlasnika);
			if(vlasnik == null) {
				ResponsePrijava data = new ResponsePrijava(null, null, true, "Ne postoji korisnik vlasnik u bazi.");
				return new ResponseEntity<ResponsePrijava>(data, HttpStatus.OK); 
			}
			
			Klub klub = klubService.dohvatiKlub(imeKluba, vlasnik);
			
			if(klub == null) {
				ResponsePrijava data = new ResponsePrijava(null, null, true, "Ne postoji klub u bazi.");
				return new ResponseEntity<ResponsePrijava>(data, HttpStatus.OK); 
			}
			// 
			Prijava prijavaFromDB = prijavaService.obrisiPrijavu(trener, klub); 
			
			if (prijavaFromDB == null) {
				ResponsePrijava data = new ResponsePrijava(null, null, false, "Neuspješno brisanje prijave");
				return new ResponseEntity<ResponsePrijava>(data, HttpStatus.OK);
			} else {
				ResponsePrijava data = new ResponsePrijava(prijavaFromDB, null, true, "Uspješno brisanje prijave");
				return new ResponseEntity<ResponsePrijava>(data, HttpStatus.OK);
			}
			}
	
	@PostMapping("/register")
	public ResponseEntity<ResponseKlub> newKlub(@RequestBody Klub newKlub, 
												@RequestParam(name = "korisnickoImeVlasnika") String korisnickoImeParam,
												@RequestParam(name= "dvorana") String dvoranaParam){
		
		String korisnickoIme = Utils.whiteSpaces(korisnickoImeParam);
		String dvorana = Utils.whiteSpaces(dvoranaParam); 
		
		Klijent vlasnik = klijentService.dohvatiKlijenta(korisnickoIme);
		if(vlasnik == null) {
			ResponseKlub data = new ResponseKlub(null, null, true, "Ne postoji korisnik u bazi.");
			return new ResponseEntity<ResponseKlub>(data, HttpStatus.OK); 
		}
		
		Dvorana newDvorana = dvoranaService.stvoriDvoranu(new Dvorana(dvorana));
		if(newDvorana == null) {
			newDvorana = new Dvorana(dvorana);
		}
		
		newKlub.setVlasnik(vlasnik);
		newKlub.setDvorana(newDvorana);
		
		Klub klubFromDB = klubService.stvoriKlub(newKlub); 
		
		if(klubFromDB == null) {
			ResponseKlub data = new ResponseKlub(null, null, false, "Neuspjesna registracija kluba");
			return new ResponseEntity<ResponseKlub>(data, HttpStatus.OK); 
		}else {
			ResponseKlub data = new ResponseKlub(klubFromDB, null, true, "Uspjesna registracija kluba");
			return new ResponseEntity<ResponseKlub>(data, HttpStatus.OK); 
		}
		
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<ResponseKlub> deleteKlub(@RequestParam(name = "korisnickoImeVlasnika") String korisnickoImeVlasnikaParam,
			@RequestParam(name= "imeKluba") String imeKlubaParam){
		
		String korisnickoImeVlasnika = Utils.whiteSpaces(korisnickoImeVlasnikaParam); 
		String imeKluba = Utils.whiteSpaces(imeKlubaParam); 
		
		Klijent vlasnik = klijentService.dohvatiKlijenta(korisnickoImeVlasnika);
		if(vlasnik == null) {
			ResponseKlub data = new ResponseKlub(null, null, true, "Ne postoji korisnik u bazi.");
			return new ResponseEntity<ResponseKlub>(data, HttpStatus.OK); 
		}
		
		Klub klubFromDB = klubService.dohvatiKlub(imeKluba, vlasnik); 
		
		if(klubFromDB == null) {
			ResponseKlub data = new ResponseKlub(null, null, true, "Ne postoji klub u bazi.");
			return new ResponseEntity<ResponseKlub>(data, HttpStatus.OK);
		}
		
		List<Long> plesnjaci = plesnjakService.dohvatiPlesnjakeFilterKlub(klubFromDB);
		for(int i = 0; i < plesnjaci.size(); i++) {
			
			Plesnjak dohvatiPlesnjak = plesnjakService.dohvatiPlesnjakById(plesnjaci.get(i));
			
			if(dohvatiPlesnjak != null) {
				List<Long> tipoviPlesa = plesnjakTipaService.dohvatiPlesnjakTipaFilterPlesnjak(dohvatiPlesnjak);
				
				for(int j = 0; j < tipoviPlesa.size(); j++) {
					TipPlesa tipPlesa = tipPlesaService.dohvatiTipPlesaById(tipoviPlesa.get(j)).get();
					if(tipPlesa != null) {
						PlesnjakTipa plesnjakTipa = plesnjakTipaService.dohvatiPlesnjakTipa(dohvatiPlesnjak, tipPlesa);
						
						List<Long> tecajevi = tecajService.dohvatiTecajeveFilterTipPlesaId(tipPlesa.getTipPlesaId()); 
						
						for(int k = 0; k < tecajevi.size(); k++) {
							Tecaj tecaj = tecajService.dohvatiTecajById(tecajevi.get(k));
							if(tecaj != null) {
								tecajService.obrisiTecaj(tecaj.getTecajId());
							}
						}
						
						if(plesnjakTipa != null) {
							
							plesnjakTipaService.obisiPlesnjakTipa(plesnjakTipa.getPlesnjakTipaId()); 
						}
					}
				}
				
				plesnjakService.obrisiPlesnjakPoId(dohvatiPlesnjak.getPlesnjakId()); 
			}
		}
		
		List<Long> tecajevi2 = tecajService.dohvatiTecajeveFilterKlub(klubFromDB); 
		
		for(int i = 0; i < tecajevi2.size(); i++) {
			Tecaj tecaj = tecajService.dohvatiTecajById(tecajevi2.get(i));
			if(tecaj != null) {
				tecajService.obrisiTecaj(tecaj.getTecajId());
			}
		}
		
		Klub klub = klubService.obrisiKlub(imeKluba, vlasnik);
		
		if(klub == null) {
			ResponseKlub data = new ResponseKlub(null, null, false, "Neuspješno brisanje kluba");
			return new ResponseEntity<ResponseKlub>(data, HttpStatus.OK);
		}else {
			ResponseKlub data = new ResponseKlub(klub, null, true, "Uspješno brisanje kluba");
			return new ResponseEntity<ResponseKlub>(data, HttpStatus.OK);
		}
	}
	
	@PutMapping("/update")
	public ResponseEntity<ResponseKlub> updatedKlub(@RequestBody Klub updatedKlub,
			@RequestParam(name = "korisnickoImeVlasnika") String korisnickoImeParam
			,@RequestParam(name = "imeKluba") String imeKlubaParam){
		
		String korisnickoIme = Utils.whiteSpaces(korisnickoImeParam); 
		String imeKluba = Utils.whiteSpaces(imeKlubaParam);
		
		Klijent vlasnik = klijentService.dohvatiKlijenta(korisnickoIme);
		if(vlasnik == null) {
			ResponseKlub data = new ResponseKlub(null, null, true, "Ne postoji korisnik u bazi.");
			return new ResponseEntity<ResponseKlub>(data, HttpStatus.OK); 
		}
		
		Klub klubFromDB = klubService.updateKlub(updatedKlub, imeKluba, vlasnik); 
		
		if(klubFromDB == null) {
			ResponseKlub data = new ResponseKlub(null, null, false, "Neuspješna izmjena kluba");
			return new ResponseEntity<ResponseKlub>(data, HttpStatus.OK);
		}else{
			ResponseKlub data = new ResponseKlub(klubFromDB, null, true, "Uspješna izmjena kluba");
			return new ResponseEntity<ResponseKlub>(data, HttpStatus.OK);
		}
		
	}
	
	@PostMapping("/register-prijava")
	public ResponseEntity<ResponsePrijava> registerPrijava(
			@RequestBody Prijava newPrijava,
			@RequestParam(name = "korisnickoImeTrenera") String korisnickoImeTreneraParam
			,@RequestParam(name = "imeKluba") String imeKlubaParam
			,@RequestParam(name = "korisnickoImeVlasnika") String korisnickoImeVlasnikaParam){
		
		String korisnickoImeTrenera = Utils.whiteSpaces(korisnickoImeTreneraParam);
		String korisnickoImeVlasnika = Utils.whiteSpaces(korisnickoImeVlasnikaParam);
		String imeKluba = Utils.whiteSpaces(imeKlubaParam); 
		
		Klijent trener = klijentService.dohvatiKlijenta(korisnickoImeTrenera);
		Klijent vlasnik = klijentService.dohvatiKlijenta(korisnickoImeVlasnika);
		
		if(trener == null) {
			ResponsePrijava data = new ResponsePrijava(null, null, true, "Ne postoji korisnik u bazi.");
			return new ResponseEntity<ResponsePrijava>(data, HttpStatus.OK); 
		}
		
		Klub klub = klubService.dohvatiKlub(imeKluba, vlasnik);
		
		if(klub == null) {
			ResponsePrijava data = new ResponsePrijava(null, null, true, "Ne postoji klub u bazi.");
			return new ResponseEntity<ResponsePrijava>(data, HttpStatus.OK); 
		}
		
		newPrijava.setKlijent(trener); 
		newPrijava.setKlub(klub); 
		//newPrijava.setJePotvrden(false); 
		
		Prijava prijavaFromDB = prijavaService.stvoriPrijavu(newPrijava); 
		
		if(prijavaFromDB == null) {
			ResponsePrijava data = new ResponsePrijava(null, null, false, "Prijava vec postoji");
			return new ResponseEntity<ResponsePrijava>(data, HttpStatus.OK); 
		}else {
			ResponsePrijava data = new ResponsePrijava(prijavaFromDB, null, true, "Uspjesna registracija prijave");
			return new ResponseEntity<ResponsePrijava>(data, HttpStatus.OK); 
		}
	}
	
	@PutMapping("/potvrdi-prijavu")
	public ResponseEntity<ResponsePrijava> potvriPrijavu(@RequestParam(name = "korisnickoImeTrenera") String korisnickoImeTreneraParam
			,@RequestParam(name = "imeKluba") String imeKlubaParam
			,@RequestParam(name = "korisnickoImeVlasnika") String korisnickoImeVlasnikaParam){
		
		String korisnickoImeTrenera = Utils.whiteSpaces(korisnickoImeTreneraParam);
		String korisnickoImeVlasnika = Utils.whiteSpaces(korisnickoImeVlasnikaParam);
		String imeKluba = Utils.whiteSpaces(imeKlubaParam); 
		
		Klijent trener = klijentService.dohvatiKlijenta(korisnickoImeTrenera);
		Klijent vlasnik = klijentService.dohvatiKlijenta(korisnickoImeVlasnika);
		
		if(trener == null) {
			ResponsePrijava data = new ResponsePrijava(null, null, true, "Ne postoji korisnik u bazi.");
			return new ResponseEntity<ResponsePrijava>(data, HttpStatus.OK); 
		}
		
		Klub klub = klubService.dohvatiKlub(imeKluba, vlasnik);
		
		if(klub == null) {
			ResponsePrijava data = new ResponsePrijava(null, null, true, "Ne postoji klub u bazi.");
			return new ResponseEntity<ResponsePrijava>(data, HttpStatus.OK); 
		}
		
		Prijava prijavaFromDB = prijavaService.dohvatiPrijavu(trener, klub); 
		
		prijavaFromDB.setJePotvrden(true); 
		prijavaFromDB = prijavaService.potvrdiPrijavu(prijavaFromDB); 
		
		ResponsePrijava data = new ResponsePrijava(prijavaFromDB, null, true, "Uspjesna potvrda prijave"); 
		return new ResponseEntity<ResponsePrijava>(data, HttpStatus.OK);
	}
	
	@PutMapping("/potvrdi")
	public ResponseEntity<ResponseKlub> potvrdiKlub(@RequestParam(name = "korisnickoImeVlasnika") String korisnickoImeVlasnikaParam,
			@RequestParam(name= "imeKluba") String imeKlubaParam){
		
		String korisnickoImeVlasnika = Utils.whiteSpaces(korisnickoImeVlasnikaParam);
		String imeKluba = Utils.whiteSpaces(imeKlubaParam);
		
		
		Klijent vlasnikFromDB = klijentService.dohvatiKlijenta(korisnickoImeVlasnika);
		if(vlasnikFromDB == null) {
			ResponseKlub data = new ResponseKlub(null, null, true, "Neuspješna potvrda kluba, vlasnik ne postoji u bazi podataka.");
			return new ResponseEntity<ResponseKlub>(data, HttpStatus.OK);
		}
		
		Klub klubFromDB = klubService.dohvatiKlub(imeKluba, vlasnikFromDB);
		if(klubFromDB == null) {
			ResponseKlub data = new ResponseKlub(null, null, true, "Neuspješna potvrda kluba, klub ne postoji u bazi podataka.");
			return new ResponseEntity<ResponseKlub>(data, HttpStatus.OK);
		}
		
		klubFromDB.setJePotvrden(true);
		klubFromDB = klubService.potvrdiKlub(klubFromDB);
		
		ResponseKlub data = new ResponseKlub(klubFromDB, null, true, "Uspješna potvrda kluba");
		return new ResponseEntity<ResponseKlub>(data, HttpStatus.OK);
	}
	
	@GetMapping("/potvrdeni-treneri")
	public ResponseEntity<ResponseKlijent> sviPotvrdeniTreneriOdKluba(
			@RequestParam(name = "klubId") Long klubId){
		
		List<Prijava> listaPrijava = prijavaService.dohvatiSvePotvrdenePrijavePoKlubu(klubId); 
		
		List<Klijent> listaTrenera = new ArrayList<Klijent>(); 
		
		for(int i = 0; i < listaPrijava.size(); i++) {
			Klijent trener = listaPrijava.get(i).getKlijent(); 
			listaTrenera.add(trener); 
		}
		
		ResponseKlijent data = new ResponseKlijent(null, listaTrenera, true, "Dohvaćanje svih potvrđenih prijava po klubu uspješno!");
		return new ResponseEntity<ResponseKlijent>(data, HttpStatus.OK); 
	}
	
	@GetMapping("/nepotvrdeni-treneri")
	public ResponseEntity<ResponseKlijent> sviNepotvrdeniTreneriOdKluba(
			@RequestParam(name = "klubId") Long klubId){
		
		List<Prijava> listaPrijava = prijavaService.dohvatiSveNepotvrdenePrijavePoKlubu(klubId); 
		
		List<Klijent> listaTrenera = new ArrayList<Klijent>(); 
		
		for(int i = 0; i < listaPrijava.size(); i++) {
			Klijent trener = listaPrijava.get(i).getKlijent(); 
			listaTrenera.add(trener); 
		}
		
		ResponseKlijent data = new ResponseKlijent(null, listaTrenera, true, "Dohvaćanje svih nepotvrđenih prijava po klubu uspješno!");
		return new ResponseEntity<ResponseKlijent>(data, HttpStatus.OK); 
	}
	
	
	//dohvati sve klubove kojima si ti trener
	
	@GetMapping("/filterTrener")
	public ResponseEntity<ResponseKlub> filtrirajPoTreneru(
			@RequestParam(name = "korisnickoIme") String korisnickoIme){
		Klijent trener = klijentService.dohvatiKlijenta(korisnickoIme);
		if(trener == null) {
			ResponseKlub data = new ResponseKlub(null, null, true, "Ne postoji trener u bazi.");
			return new ResponseEntity<ResponseKlub>(data, HttpStatus.OK);
		}
		
		List<Prijava> potvrdenePrijaveTrenera = prijavaService.dohvatiSvePotvrdenePrijavePoTreneru(trener); 
		
		List<Klub> listaKlubova = new ArrayList<Klub>(); 
		
		for(int i = 0; i < potvrdenePrijaveTrenera.size(); i++) {
			Klub klub = potvrdenePrijaveTrenera.get(i).getKlub(); 
			listaKlubova.add(klub); 
		}
		
		ResponseKlub data = new ResponseKlub(null, listaKlubova, true, "Uspješno dohvaćanje klubova prema treneru");
		return new ResponseEntity<ResponseKlub>(data, HttpStatus.OK);
	}
	
	@GetMapping("/potvrdeni")
	public ResponseEntity<ResponseKlub> sviPotvrdeniKlubovi(){
		List<Klub> listaKlubova = klubService.dohvatiSvePotvrdene(); 
		
		ResponseKlub data = new ResponseKlub(null, listaKlubova, true, "Dohvaćanje svih potvrđenih klubova uspješno!");
		return new ResponseEntity<ResponseKlub>(data, HttpStatus.OK); 
	}
	
	@GetMapping("/nepotvrdeni")
	public ResponseEntity<ResponseKlub> sviNepotvrdeniKlubovi(){
		List<Klub> listaKlubova = klubService.dohvatiSveNepotvrdene(); 
		
		ResponseKlub data = new ResponseKlub(null, listaKlubova, true, "Dohvaćanje svih nepotrvđenih klubova uspješno!");
		return new ResponseEntity<ResponseKlub>(data, HttpStatus.OK); 
	}
	
	@GetMapping("/filterKorisnik")
	public ResponseEntity<ResponseKlub> filtrirajPoKlubu(
			@RequestParam(name = "korisnickoIme") String korisnickoIme){
		Klijent vlasnik = klijentService.dohvatiKlijenta(korisnickoIme);
		if(vlasnik == null) {
			ResponseKlub data = new ResponseKlub(null, null, true, "Ne postoji korisnik u bazi.");
			return new ResponseEntity<ResponseKlub>(data, HttpStatus.OK);
		}
		
		List<Klub> klubovi = klubService.dohvatiKluboveFilterVlasnik(vlasnik);
		
		ResponseKlub data = new ResponseKlub(null, klubovi, true, "Uspješno dohvaćanje klubova prema vlasniku");
		return new ResponseEntity<ResponseKlub>(data, HttpStatus.OK);
	}
}
