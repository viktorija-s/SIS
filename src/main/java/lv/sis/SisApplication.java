package lv.sis;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import lv.sis.model.KursaDalibnieki;
import lv.sis.model.KursaDatumi;
import lv.sis.model.Kurss;
import lv.sis.model.MacibuRezultati;
import lv.sis.model.Pasniedzeji;
import lv.sis.model.Sertifikati;
import lv.sis.model.Vertejumi;
import lv.sis.model.enums.CertificateType;
import lv.sis.model.enums.Limeni;
import lv.sis.repo.IKurssRepo;
import lv.sis.repo.IKursaDalibniekiRepo;
import lv.sis.repo.IMacibuRezultatiRepo;
import lv.sis.repo.IVertejumiRepo;
import lv.sis.repo.IKursaDatumiRepo;
import lv.sis.repo.IPasniedzejiRepo;
import lv.sis.repo.ISertifikatiRepo;

@SpringBootApplication
public class SisApplication {

	public static void main(String[] args) {
		SpringApplication.run(SisApplication.class, args);
	}

	@Bean
	public CommandLineRunner testModelLayer(
			IKurssRepo kurssRepo,
			IKursaDalibniekiRepo kursaDalibniekiRepo,
			ISertifikatiRepo sertRepo,
			IPasniedzejiRepo pasnRepo,
			IVertejumiRepo vertejumiRepo,
			IKursaDatumiRepo kursaDatumiRepo,
			IMacibuRezultatiRepo macibuRezRepo) {
		return new CommandLineRunner() {

			@Override
			public void run(String... args) throws Exception {
				Kurss k1 = new Kurss("Test", 1, Limeni.Beginner);
				Kurss k2 = new Kurss("Test2", 2, Limeni.Advanced);
				kurssRepo.save(k1);
				kurssRepo.save(k2);

				KursaDalibnieki kd1 = new KursaDalibnieki("Anna", "Liepiņa", "anna@liepina.lv", "25651234", "111111-11111", "Liepāja", "Latvija", "Lāčplēša 2", 12, "LV-1234");
				KursaDalibnieki kd2 = new KursaDalibnieki("Ieva", "Varenā", "ieva@varena.lv", "21234567", "222222-22222", "Balvi", "Latvija", "Partizānu 4", 31, "LV-4580");
				kursaDalibniekiRepo.save(kd1);
				kursaDalibniekiRepo.save(kd2);

				Sertifikati s1 = new Sertifikati(CertificateType.full, LocalDate.of(2025, 6, 14), 1234, true, kd2, k2);
				Sertifikati s2 = new Sertifikati(CertificateType.participant, LocalDate.of(2025, 6, 11), 1235, false, kd1, k1);
				sertRepo.save(s1);
				sertRepo.save(s2);

				Pasniedzeji p1 = new Pasniedzeji("Anna", "Znotiņa", "anna.znotina@tdl.com", "+37120000000");
				Pasniedzeji p2 = new Pasniedzeji("Jānis", "Ozoliņš", "janis.ozolins@tdl.com", "+37121111111");
				pasnRepo.save(p1);
				pasnRepo.save(p2);
				

				KursaDatumi kdat1 = new KursaDatumi(LocalDate.of(2025, 6, 15), LocalDate.of(2025, 6, 20), k1, p2);
				kdat1.setKurss(k1);
				KursaDatumi kdat2 = new KursaDatumi(LocalDate.of(2025, 7, 1), LocalDate.of(2025, 7, 10), k2, p1);
				kdat2.setKurss(k2);

//				KursaDatumi kdat1 = new KursaDatumi(LocalDate.of(2025, 6, 15), LocalDate.of(2025, 6, 20), p1);
//				kdat1.setKurss(k1);
//				KursaDatumi kdat2 = new KursaDatumi(LocalDate.of(2025, 7, 1), LocalDate.of(2025, 7, 10), p2);
//				kdat2.setKurss(k2);


				kursaDatumiRepo.save(kdat1);
				kursaDatumiRepo.save(kdat2);
				
				Vertejumi v1 = new Vertejumi(0f, LocalDate.of(2025, 6, 12), kd1, kdat1);
				Vertejumi v2 = new Vertejumi(7.8f, LocalDate.of(2025, 5, 2), kd2, kdat2);
				vertejumiRepo.save(v1);
				vertejumiRepo.save(v2);
				
				MacibuRezultati mr1 = new MacibuRezultati("seit ir aprakstits sasniegtais macibu rezultats", k1);
				macibuRezRepo.save(mr1);
				
				MacibuRezultati mr2 = new MacibuRezultati("seit ir aprakstits sasniegtais macibu rezultats", k2);
				macibuRezRepo.save(mr2);
			}
		};
	}
}
