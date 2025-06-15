package lv.sis;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import lv.sis.model.Kurss;
import lv.sis.model.enums.Limeni;
import lv.sis.repo.ICRUDKurssRepo;

@SpringBootApplication
public class SisApplication {

	public static void main(String[] args) {
		SpringApplication.run(SisApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner testModelLayer(ICRUDKurssRepo kurssRepo) {
		return new CommandLineRunner() {
			
			@Override
			public void run(String... args) throws Exception {
				// TODO Auto-generated method stub
				Kurss k1 = new Kurss("Test", 1, Limeni.Beginner);
				Kurss k2 = new Kurss("Test2", 2, Limeni.Advanced);
				kurssRepo.save(k1);
				kurssRepo.save(k2);
				
			}
		};
	}

}
