package lv.sis;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import lv.sis.model.Kurss;
import lv.sis.model.Sertifikati;
import lv.sis.model.enums.CertificateType;
import lv.sis.model.enums.Limeni;
import lv.sis.repo.ICRUDKurssRepo;
import lv.sis.repo.SertifikatiRepo;

@SpringBootApplication
public class SisApplication {

	public static void main(String[] args) {
		SpringApplication.run(SisApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner testModelLayer(ICRUDKurssRepo kurssRepo, SertifikatiRepo sertRepo) {
		return new CommandLineRunner() {
			
			@Override
			public void run(String... args) throws Exception {
				// TODO Auto-generated method stub
				Kurss k1 = new Kurss("Test", 1, Limeni.Beginner);
				Kurss k2 = new Kurss("Test2", 2, Limeni.Advanced);
				kurssRepo.save(k1);
				kurssRepo.save(k2);
				
				
				Sertifikati s1 = new Sertifikati(CertificateType.full, LocalDate.of(2025, 6, 14), 1234, true);
				Sertifikati s2 = new Sertifikati(CertificateType.participant, LocalDate.of(2025, 6, 11), 1235, false);
				sertRepo.save(s1);
				sertRepo.save(s2);
			}
		};
	}

}
