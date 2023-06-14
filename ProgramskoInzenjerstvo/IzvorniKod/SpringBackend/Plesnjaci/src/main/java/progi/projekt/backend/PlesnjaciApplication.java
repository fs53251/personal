package progi.projekt.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PlesnjaciApplication {
	public static void main(String[] args) {
		SpringApplication.run(PlesnjaciApplication.class, args);
		System.out.println("Pokrenuto na portu: 8080");
	}
}
