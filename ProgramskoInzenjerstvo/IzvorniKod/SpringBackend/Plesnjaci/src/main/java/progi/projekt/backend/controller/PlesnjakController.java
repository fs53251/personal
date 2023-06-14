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
import progi.projekt.backend.model.TipPlesa;
import progi.projekt.backend.model.classes.Plesnjak.PlesnjakPles;
import progi.projekt.backend.model.classes.Plesnjak.ResponsePlesnjak;
import progi.projekt.backend.serviceImpl.DvoranaImpl;
import progi.projekt.backend.serviceImpl.KlijentImpl;
import progi.projekt.backend.serviceImpl.KlubImpl;
import progi.projekt.backend.serviceImpl.PlesImpl;
import progi.projekt.backend.serviceImpl.PlesnjakImpl;
import progi.projekt.backend.serviceImpl.PlesnjakTipaImpl;
import progi.projekt.backend.serviceImpl.TipPlesaImpl;
import progi.projekt.backend.utils.Utils;

@Controller
@RequestMapping("/plesnjak")
public class PlesnjakController {
	
	PlesnjakImpl plesnjakService;
	KlijentImpl klijentService;
	KlubImpl klubService;
	DvoranaImpl dvoranaService;
	PlesnjakTipaImpl plesnjakTipaService;
	PlesImpl plesService;
	TipPlesaImpl tipPlesaService; 
	
	public PlesnjakController(@Autowired PlesnjakImpl plesnjakService,
							  @Autowired KlijentImpl klijentService,
							  @Autowired KlubImpl klubService,
							  @Autowired DvoranaImpl dvoranaService,
							  @Autowired PlesnjakTipaImpl plesnjakTipaService,
							  @Autowired PlesImpl plesService,
							  @Autowired TipPlesaImpl tipPlesaService) {
		this.plesnjakService = plesnjakService;
		this.klijentService = klijentService;
		this.klubService = klubService;
		this.dvoranaService = dvoranaService;
		this.plesnjakTipaService = plesnjakTipaService;
		this.plesService = plesService;
		this.tipPlesaService = tipPlesaService; 
	}
	@GetMapping("/all")
	public ResponseEntity<ResponsePlesnjak> getAllPlesnjaci(){
		
		List<Plesnjak> listaPlesnjaka = plesnjakService.dohvatiSvePlesnjake();
		
		ResponsePlesnjak data = new ResponsePlesnjak(null, listaPlesnjaka, true, "Dohvacanje svih plesnjaka uspjesno!");
		return new ResponseEntity<ResponsePlesnjak>(data, HttpStatus.OK);
	}
	
	@PostMapping("/add")
	public ResponseEntity<ResponsePlesnjak> noviPlesnjak(@RequestBody PlesnjakPles plesnjakPles,
														 @RequestParam(name = "korisnickoImeVlasnika") String korisnickoImeVlasnikaParam,
														 @RequestParam(name = "imeKluba") String imeKlubaParam,
													   	 @RequestParam(name= "dvorana") String dvoranaParam){
		
		String korisnickoImeVlasnika = Utils.whiteSpaces(korisnickoImeVlasnikaParam); 
		String imeKluba = Utils.whiteSpaces(imeKlubaParam); 
		String dvorana = Utils.whiteSpaces(dvoranaParam); 
		
		Plesnjak newPlesnjak = plesnjakPles.getNewPlesnjak();
		List<String> tipoviPlesa = plesnjakPles.getTipoviPlesa();
		Klijent vlasnik = klijentService.dohvatiKlijenta(korisnickoImeVlasnika);
		if(vlasnik == null) {
			ResponsePlesnjak data = new ResponsePlesnjak(null, null, true, "Ne postoji korisnik u bazi.");
			return new ResponseEntity<ResponsePlesnjak>(data, HttpStatus.OK);
		}
		
		Klub klub = klubService.dohvatiKlub(imeKluba, vlasnik);
		if(klub == null) {
			ResponsePlesnjak data = new ResponsePlesnjak(null, null, true, "Ne postoji klub u bazi.");
			return new ResponseEntity<ResponsePlesnjak>(data, HttpStatus.OK);
		}
		
		Dvorana dohvacenaDvorana = dvoranaService.dohvatiDvoranu(dvorana);
		if(dohvacenaDvorana == null) {
			dohvacenaDvorana = dvoranaService.stvoriDvoranu(new Dvorana(dvorana));
		}
		
		List<String> nepostojeci = new ArrayList<>();
		List<TipPlesa> plesovi = new ArrayList<>();
		for(String ples: tipoviPlesa) {
			TipPlesa tipPlesa = null;
			if((tipPlesa = plesService.dohvatiPles(ples)) == null) {
				nepostojeci.add(ples);
			}else {
				plesovi.add(tipPlesa);
			}
		}
		
		if(nepostojeci.size() > 0) {
			StringBuilder sb = new StringBuilder();
			nepostojeci.forEach((el) -> sb.append(el+" "));
			ResponsePlesnjak data = new ResponsePlesnjak(null, null, false, String.format("Ne postoje sljedeci ples/plesovi u bazi: %s", sb.toString()));
			return new ResponseEntity<ResponsePlesnjak>(data, HttpStatus.OK); 
		}
		
		newPlesnjak.setKlubOrganizator(klub);
		newPlesnjak.setDvorana(dohvacenaDvorana);
		
		Plesnjak plesnjakFromDB = plesnjakService.stvoriPlesnjak(newPlesnjak);
		
		if(plesnjakFromDB == null) {
			ResponsePlesnjak data = new ResponsePlesnjak(null, null, false, "Neuspjesna registracija plesnjaka");
			return new ResponseEntity<ResponsePlesnjak>(data, HttpStatus.OK); 
		}else {
			ResponsePlesnjak data = null;
			if(!spremiKorelacijuPlesnjakPles(plesnjakFromDB, plesovi)){
				 data = new ResponsePlesnjak(plesnjakFromDB, null, true, "Uspjesna registracija plesnjaka, neuspjelo spremanje u bazu korelacije plesnjak - ples");
			}else {
				 data = new ResponsePlesnjak(plesnjakFromDB, null, true, "Uspjesna registracija plesnjaka");
			}
			return new ResponseEntity<ResponsePlesnjak>(data, HttpStatus.OK); 
		}
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<ResponsePlesnjak> deletePlesnjak(@RequestParam(name= "plesnjakId") Long plesnjakId){
		
		Plesnjak plesnjakFromDB = plesnjakService.dohvatiPlesnjakById(plesnjakId); 
		
		if(plesnjakFromDB == null) {
			ResponsePlesnjak data = new ResponsePlesnjak(null, null, false, "Neuspješno dohvaćanje plesnjaka po id-u");
			return new ResponseEntity<ResponsePlesnjak>(data, HttpStatus.OK);
		}
		
		List<Long> tipoviPlesa = plesnjakTipaService.dohvatiPlesnjakTipaFilterPlesnjak(plesnjakFromDB);
		
		for(int i = 0; i < tipoviPlesa.size(); i++) {
			//greska
			TipPlesa tipPlesa = tipPlesaService.dohvatiTipPlesaById(tipoviPlesa.get(i)).get();
			if(tipPlesa != null) {
				PlesnjakTipa plesnjakTipa = plesnjakTipaService.dohvatiPlesnjakTipa(plesnjakFromDB, tipPlesa);
				if(plesnjakTipa != null) {
					plesnjakTipaService.obisiPlesnjakTipa(plesnjakTipa.getPlesnjakTipaId()); 
				}
			}
		}
		
		plesnjakFromDB = plesnjakService.obrisiPlesnjakPoId(plesnjakId); 
		
		if(plesnjakFromDB == null) {
			ResponsePlesnjak data = new ResponsePlesnjak(null, null, false, "Neuspješno brisanje plesnjaka");
			return new ResponseEntity<ResponsePlesnjak>(data, HttpStatus.OK);
		}else {
			ResponsePlesnjak data = new ResponsePlesnjak(plesnjakFromDB, null, true, "Uspješno brisanje plesnjaka");
			return new ResponseEntity<ResponsePlesnjak>(data, HttpStatus.OK);
		}
	}
	
	@GetMapping("/fetch")
	public ResponseEntity<ResponsePlesnjak> getPlesnjak(@RequestParam(name= "plesnjakId") Long plesnjakId){
		
		Plesnjak plesnjakFromDB = plesnjakService.dohvatiPlesnjakById(plesnjakId);
		
		if(plesnjakFromDB == null) {
			ResponsePlesnjak data = new ResponsePlesnjak(null, null, false, "Neuspješno dohvaćanje plesnjaka");
			return new ResponseEntity<ResponsePlesnjak>(data, HttpStatus.OK);
		}else{
			ResponsePlesnjak data = new ResponsePlesnjak(plesnjakFromDB, null, true, "Uspješno dohvaćanje plesnjaka");
			return new ResponseEntity<ResponsePlesnjak>(data, HttpStatus.OK);
		}
		
	}
	
	@PutMapping("/update")
	public ResponseEntity<ResponsePlesnjak> updatedPlesnjak(@RequestBody Plesnjak updatedPlesnjak, @RequestParam(required = false, name = "adresa") String adresa){
		
		if(updatedPlesnjak.getPlesnjakId() == null) {
			ResponsePlesnjak data = new ResponsePlesnjak(null, null, false, "Nedostaje PlesnjakId");
			return new ResponseEntity<ResponsePlesnjak>(data, HttpStatus.OK);
		}
		
		Dvorana dvoranaFromDB = dvoranaService.dohvatiDvoranu(adresa); 
		
		if(dvoranaFromDB == null) {
			 dvoranaFromDB = dvoranaService.stvoriDvoranu(new Dvorana(adresa)); 
		}
		
		Plesnjak plesnjakFromDB = plesnjakService.updatePlesnjakId(updatedPlesnjak, dvoranaFromDB); 
		
		if(plesnjakFromDB == null) {
			ResponsePlesnjak data = new ResponsePlesnjak(null, null, false, "Neuspješna izmjena plesnjaka");
			return new ResponseEntity<ResponsePlesnjak>(data, HttpStatus.OK);
		}else{
			ResponsePlesnjak data = new ResponsePlesnjak(plesnjakFromDB, null, true, "Uspješna izmjena plesnjaka");
			return new ResponseEntity<ResponsePlesnjak>(data, HttpStatus.OK);
		}
		
	}
	
	
	@GetMapping("/filterPles")
	public ResponseEntity<ResponsePlesnjak> filtrirajPoTipuPlesa(@RequestParam(name = "nazivPlesa") String nazivPlesaParam){
		
		String nazivPlesa = Utils.whiteSpaces(nazivPlesaParam); 
		
		TipPlesa ples = plesService.dohvatiPles(nazivPlesa);
		if(ples == null) {
			ResponsePlesnjak data = new ResponsePlesnjak(null, null, false, "Ne postoji traženi ples u bazi.");
			return new ResponseEntity<ResponsePlesnjak>(data, HttpStatus.OK);
		}
		
		List<Long> plesnjaci = plesnjakTipaService.dohvatiPlesnjakeFilterPles(ples);
		List<Plesnjak> listaPlesnjaka = new ArrayList<>();
		for(int i = 0; i < plesnjaci.size(); i++) {
			Plesnjak dohvatiPlesnjak = plesnjakService.dohvatiPlesnjakById(plesnjaci.get(i));
			if(dohvatiPlesnjak != null) {
				listaPlesnjaka.add(dohvatiPlesnjak);
			}
		}
		ResponsePlesnjak data = new ResponsePlesnjak(null, listaPlesnjaka, true, "Uspješno dohvaćanje plesnjaka prema plesu");
		return new ResponseEntity<ResponsePlesnjak>(data, HttpStatus.OK);
	}
	
	@GetMapping("/filterKlub")
	public ResponseEntity<ResponsePlesnjak> filtrirajPoKlubu(
			@RequestParam(name = "korisnickoImeVlasnika") String korisnickoImeVlasnikaParam,
			@RequestParam(name = "imeKluba") String imeKlubaParam){
		
		String korisnickoImeVlasnika = Utils.whiteSpaces(korisnickoImeVlasnikaParam); 
		String imeKluba = Utils.whiteSpaces(imeKlubaParam);
		
		Klijent vlasnik = klijentService.dohvatiKlijenta(korisnickoImeVlasnika);
		if(vlasnik == null) {
			ResponsePlesnjak data = new ResponsePlesnjak(null, null, true, "Ne postoji korisnik u bazi.");
			return new ResponseEntity<ResponsePlesnjak>(data, HttpStatus.OK);
		}
		Klub klub = klubService.dohvatiKlub(imeKluba, vlasnik);
		if(klub == null) {
			ResponsePlesnjak data = new ResponsePlesnjak(null, null, true, "Ne postoji klub u bazi.");
			return new ResponseEntity<ResponsePlesnjak>(data, HttpStatus.OK);
		}
		
		List<Long> plesnjaci = plesnjakService.dohvatiPlesnjakeFilterKlub(klub);
		List<Plesnjak> listaPlesnjaka = new ArrayList<>();
		for(int i = 0; i < plesnjaci.size(); i++) {
			Plesnjak dohvatiPlesnjak = plesnjakService.dohvatiPlesnjakById(plesnjaci.get(i));
			if(dohvatiPlesnjak != null) {
				listaPlesnjaka.add(dohvatiPlesnjak);
			}
		}
		ResponsePlesnjak data = new ResponsePlesnjak(null, listaPlesnjaka, true, "Uspješno dohvaćanje plesnjaka prema klubu");
		return new ResponseEntity<ResponsePlesnjak>(data, HttpStatus.OK);
	}
	
	private boolean spremiKorelacijuPlesnjakPles(Plesnjak plesnjakFromDB, List<TipPlesa> plesovi) {
		boolean spremljeno = true;
		for(TipPlesa ples : plesovi) {
			if(plesnjakTipaService.spremiPlesnjakPles(plesnjakFromDB, ples) == null) {
				spremljeno = false;
			}
		}
		return spremljeno;
	}
}
