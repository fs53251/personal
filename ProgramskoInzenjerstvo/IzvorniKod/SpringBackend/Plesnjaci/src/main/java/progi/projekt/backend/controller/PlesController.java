package progi.projekt.backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import progi.projekt.backend.model.Klub;
import progi.projekt.backend.model.Plesnjak;
import progi.projekt.backend.model.PlesnjakTipa;
import progi.projekt.backend.model.Tecaj;
import progi.projekt.backend.model.TipPlesa;
import progi.projekt.backend.model.classes.Ples.ResponsePles;
import progi.projekt.backend.model.classes.Ples.ResponsePlesTecaj;
import progi.projekt.backend.serviceImpl.KlubImpl;
import progi.projekt.backend.serviceImpl.PlesImpl;
import progi.projekt.backend.serviceImpl.PlesnjakImpl;
import progi.projekt.backend.serviceImpl.PlesnjakTipaImpl;
import progi.projekt.backend.serviceImpl.TecajImpl;
import progi.projekt.backend.utils.Utils;



@Controller
@RequestMapping("/ples")
public class PlesController {
	
	PlesImpl plesService;
	TecajImpl tecajService; 
	PlesnjakTipaImpl plesnjakTipaService;
	PlesnjakImpl plesnjakService; 
	KlubImpl klubService;
	
	
	public PlesController(@Autowired PlesImpl plesService, @Autowired TecajImpl tecajService, @Autowired PlesnjakTipaImpl plesnjakTipaService,
			@Autowired PlesnjakImpl plesnjakService, @Autowired KlubImpl klubService) {
		this.plesService = plesService;
		this.tecajService = tecajService; 
		this.plesnjakTipaService = plesnjakTipaService;
		this.plesnjakService = plesnjakService; 
		this.klubService = klubService; 
	}
	
	@GetMapping("/klub/tecajevi")
    public ResponseEntity<ResponsePlesTecaj> dohvatiPlesoveZaKlubTecaj(@RequestParam(name = "klubId") Long klubId){
        Klub klub = klubService.dohvatiKlubPoId(klubId);
        if(klub == null) {
            ResponsePlesTecaj data = new ResponsePlesTecaj(null, true, "Neuspješno dohvaćanje kluba");
            return new ResponseEntity<ResponsePlesTecaj>(data, HttpStatus.OK);
        }
        List<String> plesovi = new ArrayList<>();
        List<Long> tecajevi = tecajService.dohvatiTecajeveFilterKlub(klub);
        if(tecajevi.size() > 0) {
            for(Long id : tecajevi) {
                Tecaj tecaj = tecajService.dohvatiTecajById(id);
                if(!plesovi.contains(tecaj.getTipPlesa().getNaziv())) //da bude unikatan
                	plesovi.add(tecaj.getTipPlesa().getNaziv());
            }
        }
        ResponsePlesTecaj data = new ResponsePlesTecaj(plesovi, true, "Uspješno dohvaćanje kluba");
        return new ResponseEntity<ResponsePlesTecaj>(data, HttpStatus.OK);
    }
	
	@GetMapping("/ples/plesnjaci")
	public ResponseEntity<ResponsePlesTecaj> dohvatiPlesoveZaPlesnjak(@RequestParam(name = "plesnjakId") Long plesnjakId){
		
		Plesnjak plesnjak = plesnjakService.dohvatiPlesnjakById(plesnjakId);
		
		if(plesnjak == null) {
            ResponsePlesTecaj data = new ResponsePlesTecaj(null, true, "Neuspješno dohvaćanje plesnjaka");
            return new ResponseEntity<ResponsePlesTecaj>(data, HttpStatus.OK);
        }
		List<Long> plesId = plesnjakTipaService.dohvatiPlesnjakTipaFilterPlesnjak(plesnjak); //ovo je vec lista ples id-a
		List<String> listaPlesova = new ArrayList<>(); 
		
		if(plesId.size() > 0) {
			for(Long id: plesId) {
				TipPlesa ples = plesService.dohvatiPlesPoId(id); 
				String nazivPlesa = ples.getNaziv();
				if(nazivPlesa != null) {
					listaPlesova.add(nazivPlesa); 
				}
			}
		}
		
		ResponsePlesTecaj data = new ResponsePlesTecaj(listaPlesova, true, "Uspješno dohvaćanje plesova");
        return new ResponseEntity<ResponsePlesTecaj>(data, HttpStatus.OK);
	}
	
	@GetMapping("/all")
	public ResponseEntity<ResponsePles> getAllPlesovi(){
		
		List<TipPlesa> listaPlesova = plesService.dohvatiSvePlesove(); 
		
		if(listaPlesova.isEmpty()) {
			ResponsePles data = new ResponsePles(null, listaPlesova, false, "Dohvacanje svih plesova neuspjesno - ne postoje plesovi!"); 
			return new ResponseEntity<ResponsePles>(data, HttpStatus.OK); 
		}else {
			ResponsePles data = new ResponsePles(null, listaPlesova, true, "Dohvacanje svih plesova uspjesno!");
			return new ResponseEntity<ResponsePles>(data, HttpStatus.OK); 
		}
	}
	
	@GetMapping("/fetch/{name}")
	public ResponseEntity<ResponsePles> getPles(@PathVariable(name = "name") String imePlesaParam){
		
		String imePlesa = Utils.whiteSpaces(imePlesaParam); 
		
		TipPlesa plesFromDB = plesService.dohvatiPles(imePlesa);
		
		if(plesFromDB == null) {
			ResponsePles data = new ResponsePles(null, null, false, "Neuspješno dohvaćanje plesa");
			return new ResponseEntity<ResponsePles>(data, HttpStatus.OK);
		}else{
			ResponsePles data = new ResponsePles(plesFromDB, null, true, "Uspješno dohvaćanje plesa");
			return new ResponseEntity<ResponsePles>(data, HttpStatus.OK);
		}
		
	}
	
	
	@PostMapping("/add")
	public ResponseEntity<ResponsePles> noviPles(@RequestBody TipPlesa noviPles) {
		
		TipPlesa plesFromDB = plesService.stvoriPles(noviPles);
		
		if(plesFromDB == null) {
			ResponsePles data = new ResponsePles(null, null, false, "Neuspješno stvaranje plesa");
			return new ResponseEntity<ResponsePles>(data, HttpStatus.OK);
		}else{
			ResponsePles data = new ResponsePles(plesFromDB, null, true, "Uspješno stvaranje plesa");
			return new ResponseEntity<ResponsePles>(data, HttpStatus.OK);
		}
	}
	
	@GetMapping("/delete/{name}")
	public ResponseEntity<ResponsePles> obrisiPles(@PathVariable(name = "name") String imePlesaParam) {
		
		String imePlesa = Utils.whiteSpaces(imePlesaParam); 
		TipPlesa plesFromDB = plesService.dohvatiPles(imePlesa); 
		if(plesFromDB == null) {
			ResponsePles data = new ResponsePles(null, null, false, "Neuspješno dohvaćanje plesa po imenu plesa");
			return new ResponseEntity<ResponsePles>(data, HttpStatus.OK);
		}
		//brisanje tecaja 
		List<Long> tecajevi = tecajService.dohvatiTecajeveFilterTipPlesaId(plesFromDB.getTipPlesaId()); 
		
		for(int i = 0; i < tecajevi.size(); i++) {
			Tecaj tecaj = tecajService.dohvatiTecajById(tecajevi.get(i));
			if(tecaj != null) {
				tecajService.obrisiTecaj(tecaj.getTecajId());
			}
		}
		
		//brisanje veze plesnjak_tipa 
		if(plesFromDB != null) {
			List<Long> plesnjaci = plesnjakTipaService.dohvatiPlesnjakeFilterPles(plesFromDB); 
			
			for(int i = 0; i < plesnjaci.size(); i++) {
				Plesnjak plesnjak = plesnjakService.dohvatiPlesnjakById(plesnjaci.get(i)); 
				if(plesnjak != null) {
					PlesnjakTipa plesnjakTipa = plesnjakTipaService.dohvatiPlesnjakTipa(plesnjak, plesFromDB);
					if(plesnjakTipa != null) {
						plesnjakTipaService.obisiPlesnjakTipa(plesnjakTipa.getPlesnjakTipaId()); 
					}
				}
			}
			
		}
		
		//brisanje plesa
		plesFromDB = plesService.obrisiPles(imePlesa);
		
		if(plesFromDB == null) {
			ResponsePles data = new ResponsePles(null, null, false, "Neuspješno brisanje plesa");
			return new ResponseEntity<ResponsePles>(data, HttpStatus.OK);
		}else{
			ResponsePles data = new ResponsePles(plesFromDB, null, true, "Uspješno brisanje plesa");
			return new ResponseEntity<ResponsePles>(data, HttpStatus.OK);
		}
	}
	
	@PostMapping("/update/{name}")
	public ResponseEntity<ResponsePles> izmjeniPles(@PathVariable(name = "name") String imePlesaParam, @RequestBody TipPlesa ples) {
		
		String imePlesa = Utils.whiteSpaces(imePlesaParam); 
		
		TipPlesa plesFromDB = plesService.updatePles(ples, imePlesa);
		
		if(plesFromDB == null) {
			ResponsePles data = new ResponsePles(null, null, false, "Neuspješna izmjena plesa");
			return new ResponseEntity<ResponsePles>(data, HttpStatus.OK);
		}else{
			ResponsePles data = new ResponsePles(plesFromDB, null, true, "Uspješna izmjena plesa");
			return new ResponseEntity<ResponsePles>(data, HttpStatus.OK);
		}
	}
}
