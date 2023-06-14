package progi.projekt.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import progi.projekt.backend.model.Klijent;
import progi.projekt.backend.model.classes.Klijent.LoginKlijent;
import progi.projekt.backend.model.classes.Klijent.ResponseKlijent;
import progi.projekt.backend.serviceImpl.KlijentImpl;
import progi.projekt.backend.utils.Utils;

@Controller
@RequestMapping("/user")
public class UserController {

	KlijentImpl userService;

	public UserController(@Autowired KlijentImpl userService) {
		this.userService = userService;
	}
	
	@GetMapping("/fetch/{username}")
	public ResponseEntity<ResponseKlijent> getKlijent(@PathVariable(name = "username") String usernameParam){
		
		String username = Utils.whiteSpaces(usernameParam); 
		
		Klijent osoba = userService.dohvatiKlijenta(username); 
		
		if (osoba == null) {
			ResponseKlijent data = new ResponseKlijent(null, null, false, "Dohvat neuspješan");
			return new ResponseEntity<ResponseKlijent>(data, HttpStatus.OK);
		} else {
			ResponseKlijent data = new ResponseKlijent(osoba, null, true, "Dohvat uspješan");
			return new ResponseEntity<ResponseKlijent>(data, HttpStatus.OK);
		}
	}

	@GetMapping("/all")
	public ResponseEntity<ResponseKlijent> getAllKlijenti(){
		
		List<Klijent> listaKlijenata = userService.dohvatiSveKlijente(); 
		
		if(listaKlijenata.isEmpty()) {
			ResponseKlijent data = new ResponseKlijent(null, listaKlijenata, true, "Dohvat uspješan");
			return new ResponseEntity<ResponseKlijent>(data, HttpStatus.OK);
		}else {
			ResponseKlijent data = new ResponseKlijent(null, listaKlijenata, true, "Dohvacanje svih klijenata uspjesno!");
			return new ResponseEntity<ResponseKlijent>(data, HttpStatus.OK);
		}
	}


	@PostMapping("/login")
	public ResponseEntity<ResponseKlijent> loginKlijent(@RequestBody LoginKlijent user) {

		Klijent osoba = userService.dohvatiKlijentaLozinka(user.getUsername(), user.getPassword());

		if (osoba == null) {
			ResponseKlijent data = new ResponseKlijent(null, null, false, "Prijava neuspješna");
			return new ResponseEntity<ResponseKlijent>(data, HttpStatus.OK);
		} else {
			ResponseKlijent data = new ResponseKlijent(osoba, null, true, "Prijava uspješna");
			return new ResponseEntity<ResponseKlijent>(data, HttpStatus.OK);
		}
	}

	@PostMapping("/register")
	public ResponseEntity<ResponseKlijent> newKlijent(@RequestBody Klijent newUser) {

		Klijent userFromDB = userService.stvoriKlijenta(newUser);

		if (userFromDB == null) {
			ResponseKlijent data = new ResponseKlijent(null, null, false, "Neuspješno stvaranje korisnika");
			return new ResponseEntity<ResponseKlijent>(data, HttpStatus.OK);
		} else {
			ResponseKlijent data = new ResponseKlijent(userFromDB, null, true, "Uspješno stvaranje korisnika");
			return new ResponseEntity<ResponseKlijent>(data, HttpStatus.OK);
		}
	}

	@PutMapping("/update")
	public ResponseEntity<ResponseKlijent> updatedKlijent(@RequestBody Klijent updatedKlijent) {

		Klijent userFromDB = userService.updateKlijenta(updatedKlijent);

		if (userFromDB == null) {
			ResponseKlijent data = new ResponseKlijent(null, null, false, "Neuspješno updateanje korisnika");
			return new ResponseEntity<ResponseKlijent>(data, HttpStatus.OK);
		} else {
			ResponseKlijent data = new ResponseKlijent(userFromDB, null, true, "Uspješno updateanje korisnika");
			return new ResponseEntity<ResponseKlijent>(data, HttpStatus.OK);
		}
	}

	@DeleteMapping("/delete/{username}")
	public ResponseEntity<ResponseKlijent> deleteKlijent(@PathVariable(name = "username") String usernameParam) {
		
		String username = Utils.whiteSpaces(usernameParam); 
		
		Klijent klijentFromDB = userService.obrisiKlijenta(username);

		if (klijentFromDB == null) {
			ResponseKlijent data = new ResponseKlijent(null, null, false, "Neuspješno brisanje klijenta");
			return new ResponseEntity<ResponseKlijent>(data, HttpStatus.OK);
		} else {
			ResponseKlijent data = new ResponseKlijent(klijentFromDB, null, true, "Uspješno brisanje klijenta");
			return new ResponseEntity<ResponseKlijent>(data, HttpStatus.OK);
		}
	}

	@PostMapping("/changePassword")
	public ResponseEntity<ResponseKlijent> changePasswordKlijent(@RequestBody Map<String, String> map) {
		String korisnickoIme = map.get("korisnicko_ime");
		String staraLozinka = map.get("stara_lozinka");
		String novaLozinka = map.get("nova_lozinka");

		Klijent userFromDB = userService.dohvatiKlijentaLozinka(korisnickoIme, staraLozinka);

		if (userFromDB == null) {
			ResponseKlijent data = new ResponseKlijent(null, null, false, "Invalid password!");
			return new ResponseEntity<ResponseKlijent>(data, HttpStatus.OK);
		} else {
			Klijent updateUser = userService.updatePassword(userFromDB, novaLozinka);

			ResponseKlijent data = new ResponseKlijent(updateUser, null,  true, "Password changed successfully");
			return new ResponseEntity<ResponseKlijent>(data, HttpStatus.OK);
		}
	}
	
	@GetMapping("/tipKorisnika/izmjena")
	public ResponseEntity<ResponseKlijent> changeTipKorisnika(@RequestParam(name = "username") String username){
		Klijent userFromDB = userService.dohvatiKlijenta(username);
		if(userFromDB.getTipKorisnika().toLowerCase().equals("klijent")) {
			userFromDB.setTipKorisnika("Administrator");
		}else if(userFromDB.getTipKorisnika().toLowerCase().equals("administrator")) {
			userFromDB.setTipKorisnika("Klijent");
		}
		
		Klijent noviKlijent = userService.updateKlijenta(userFromDB);

		ResponseKlijent data = new ResponseKlijent(noviKlijent, null,  true, "Uspješna izmjena tipa korisnika");
		return new ResponseEntity<ResponseKlijent>(data, HttpStatus.OK);
	}

}
