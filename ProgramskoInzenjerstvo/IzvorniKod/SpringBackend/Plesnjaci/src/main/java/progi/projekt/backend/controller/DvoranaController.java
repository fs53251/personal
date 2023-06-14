package progi.projekt.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import progi.projekt.backend.model.Dvorana;
import progi.projekt.backend.model.classes.Dvorana.ResponseDvorana;
import progi.projekt.backend.serviceImpl.DvoranaImpl;
import progi.projekt.backend.utils.Utils;

@Controller
@RequestMapping("/dvorana")
public class DvoranaController {
	
	DvoranaImpl dvoranaService;
	
	public DvoranaController(@Autowired DvoranaImpl dvoranaService) {
		this.dvoranaService = dvoranaService;
	}
	
	@GetMapping("/all")
	public ResponseEntity<ResponseDvorana> getAllDvorane(){
		List<Dvorana> listaDvorana = dvoranaService.dohvatiDvorane();
		
		ResponseDvorana data = new ResponseDvorana(null, listaDvorana, true, "Dohvacanje svih dvorana uspjesno");
		return new ResponseEntity<ResponseDvorana>(data, HttpStatus.OK);
	}
	
	@GetMapping("/create")
	public ResponseEntity<ResponseDvorana> newDvorana(@RequestBody Dvorana novaDvorana){
		
		Dvorana dvoranaFromDB = dvoranaService.stvoriDvoranu(novaDvorana);
		
		if(dvoranaFromDB == null) {
			ResponseDvorana data = new ResponseDvorana(null, null, false, "Neuspješno stvaranje dvorane");
			return new ResponseEntity<ResponseDvorana>(data, HttpStatus.OK);
		}else {
			ResponseDvorana data = new ResponseDvorana(dvoranaFromDB, null, false, "Neuspješno stvaranje dvorane");
			return new ResponseEntity<ResponseDvorana>(data, HttpStatus.OK);
		}
	}
	
	@DeleteMapping("/delete/{adresa}")
	public ResponseEntity<ResponseDvorana> deleteDvorana(@PathVariable(name = "adresa") String adresaParam){
		
		String adresa = Utils.whiteSpaces(adresaParam); 
		
		Dvorana dvorana = dvoranaService.dohvatiDvoranu(adresa);
		if(dvorana == null) {
			ResponseDvorana data = new ResponseDvorana(null, null, false, "Neuspješno brisanje dvorane, dvorana ne postoji u bazi");
			return new ResponseEntity<ResponseDvorana>(data, HttpStatus.OK);
		}
		Dvorana dvoranaFromDB = dvoranaService.obrisiDvoranu(dvorana);
		
		if(dvoranaFromDB == null) {
			ResponseDvorana data = new ResponseDvorana(null, null, false, "Neuspješno brisanje dvorane");
			return new ResponseEntity<ResponseDvorana>(data, HttpStatus.OK);
		}else {
			ResponseDvorana data = new ResponseDvorana(dvoranaFromDB, null, false, "Neuspješno brisanje dvorane");
			return new ResponseEntity<ResponseDvorana>(data, HttpStatus.OK);
		}
	}
}
