package lv.sis;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import lv.sis.model.KursaDalibnieki;
import lv.sis.model.Kurss;
import lv.sis.model.enums.Limeni;
import lv.sis.repo.ICRUDKurssRepo;
import lv.sis.repo.IKursaDalibniekiRepo;

@SpringBootApplication
public class SisApplication {

	public static void main(String[] args) {
		SpringApplication.run(SisApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner testModelLayer(ICRUDKurssRepo kurssRepo, IKursaDalibniekiRepo kursaDalibniekiRepo) {
		return new CommandLineRunner() {
			
			@Override
			public void run(String... args) throws Exception {
				// TODO Auto-generated method stub
				Kurss k1 = new Kurss("Test", 1, Limeni.Beginner);
				Kurss k2 = new Kurss("Test2", 2, Limeni.Advanced);
				kurssRepo.save(k1);
				kurssRepo.save(k2);
				
				KursaDalibnieki kd1 = new KursaDalibnieki("Anna", "Liepiņa", "anna@liepina.lv", "25651234", "111111-11111", "Liepāja", "Latvija", "Lāčplēša 2", 12, "LV-1234");
				KursaDalibnieki kd2 = new KursaDalibnieki("Ieva", "Varenā", "ieva@varena.lv", "21234567", "222222-22222", "Balvi", "Latvija", "Partizānu 4", 31, "LV-4580");
				kursaDalibniekiRepo.save(kd1);
				kursaDalibniekiRepo.save(kd2);
				
			}
			
		};
	}

}
