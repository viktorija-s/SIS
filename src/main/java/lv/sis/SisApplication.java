package lv.sis;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import lv.sis.model.KursaDalibnieki;
import lv.sis.model.KursaDatumi;
import lv.sis.model.Kurss;
import lv.sis.model.MyAuthority;
import lv.sis.model.MyUser;
import lv.sis.model.MacibuRezultati;
import lv.sis.model.Pasniedzeji;
import lv.sis.model.Vertejumi;
import lv.sis.model.enums.Limeni;
import lv.sis.repo.IKurssRepo;
import lv.sis.repo.IKursaDalibniekiRepo;
import lv.sis.repo.IMyAuthorityRepo;
import lv.sis.repo.IMyUserRepo;
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
	public CommandLineRunner testModelLayer(IKurssRepo kurssRepo, IKursaDalibniekiRepo kursaDalibniekiRepo,
			ISertifikatiRepo sertRepo, IPasniedzejiRepo pasnRepo, IVertejumiRepo vertejumiRepo,
			IKursaDatumiRepo kursaDatumiRepo, IMyAuthorityRepo authRepo, IMyUserRepo userRepo,
			IMacibuRezultatiRepo macibuRezRepo) {
		return new CommandLineRunner() {

			@Override
			public void run(String... args) throws Exception {
				Kurss k1 = new Kurss("Introduction to Python", 15, Limeni.Beginner);
				Kurss k2 = new Kurss("Web Development with HTML & CSS", 12, Limeni.Junior);
				Kurss k3 = new Kurss("Introduction to SQL", 15, Limeni.Intermediate);
				Kurss k4 = new Kurss("Data Analysis with Excel", 10, Limeni.Intermediate);
				Kurss k5 = new Kurss("Java Programming Basics", 18, Limeni.Beginner);
				Kurss k6 = new Kurss("Digital Marketing Fundamentals", 8, Limeni.Junior);
				Kurss k7 = new Kurss("Project Management Essentials", 20, Limeni.Intermediate);
				Kurss k8 = new Kurss("Graphic Design Basics", 16, Limeni.Junior);
				kurssRepo.save(k1);
				kurssRepo.save(k2);
				kurssRepo.save(k3);
				kurssRepo.save(k4);
				kurssRepo.save(k5);
				kurssRepo.save(k6);
				kurssRepo.save(k7);
				kurssRepo.save(k8);

				KursaDalibnieki kd1 = new KursaDalibnieki("Anna", "Liepiņa", "anna@liepina.lv", "25651234",
						"111111-11111", "Liepāja", "Latvija", "Lāčplēša 2", 12, "LV-1234");
				KursaDalibnieki kd2 = new KursaDalibnieki("Ieva", "Varenā", "ieva@varena.lv", "21234567",
						"222222-22222", "Balvi", "Latvija", "Partizānu 4", 31, "LV-4580");
				KursaDalibnieki kd3 = new KursaDalibnieki("Elīna", "Muceniece", "elina.muceniece@example.com",
						"+37129123456", "010101-12345", "Rīga", "Latvija", "Dzirnavu iela 15", 4, "LV-1050");
				KursaDalibnieki kd4 = new KursaDalibnieki("Raimonds", "Kalniņš", "raimonds.kalnins@example.com",
						"+37129876543", "020202-23456", "Liepāja", "Latvija", "Brīvības iela 23", 8, "LV-3401");
				KursaDalibnieki kd5 = new KursaDalibnieki("Maija", "Ozola", "maija.ozola@example.com", "+37129223344",
						"030303-34567", "Daugavpils", "Latvija", "Stacijas iela 7", 12, "LV-5401");
				KursaDalibnieki kd6 = new KursaDalibnieki("Jānis", "Bērziņš", "janis.berzins@example.com",
						"+37129556677", "040404-45678", "Valmiera", "Latvija", "Pils iela 2", 3, "LV-4201");
				KursaDalibnieki kd7 = new KursaDalibnieki("Līga", "Zariņa", "liga.zarina@example.com", "+37129667788",
						"050505-56789", "Jelgava", "Latvija", "Saules iela 19", 10, "LV-3005");
				KursaDalibnieki kd8 = new KursaDalibnieki("Roberts", "Grīnbergs", "roberts.grinbergs@example.com",
						"+37129778899", "060606-67890", "Cēsis", "Latvija", "Meža iela 6", 1, "LV-4100");
				kursaDalibniekiRepo.save(kd1);
				kursaDalibniekiRepo.save(kd2);
				kursaDalibniekiRepo.save(kd3);
				kursaDalibniekiRepo.save(kd4);
				kursaDalibniekiRepo.save(kd5);
				kursaDalibniekiRepo.save(kd6);
				kursaDalibniekiRepo.save(kd7);
				kursaDalibniekiRepo.save(kd8);

				PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

				MyAuthority auth1 = new MyAuthority("PROFESSOR");
				MyAuthority auth2 = new MyAuthority("ADMIN");
				authRepo.save(auth1);
				authRepo.save(auth2);

				MyUser u1 = new MyUser("admin", encoder.encode("admin"), auth2);
				MyUser u2 = new MyUser("user", encoder.encode("user"), auth1); // pid = 1
				MyUser u3 = new MyUser("user2", encoder.encode("user2"), auth1); // pid = 2
				userRepo.save(u1);
				userRepo.save(u2);
				userRepo.save(u3);

				Pasniedzeji p1 = new Pasniedzeji("Anna", "Znotiņa", "anna.znotina@tdl.com", "+37120000000");
				Pasniedzeji p2 = new Pasniedzeji("Jānis", "Ozoliņš", "janis.ozolins@tdl.com", "+37121111111");
				Pasniedzeji p3 = new Pasniedzeji("Ilze", "Bērziņa", "ilze.berzina@tdl.com", "+37122222222");
				Pasniedzeji p4 = new Pasniedzeji("Jānis", "Kalniņš", "janis.kalnins@tdl.com", "+37123333333");
				Pasniedzeji p5 = new Pasniedzeji("Līga", "Ozoliņa", "liga.ozolina@tdl.com", "+37124444444");
				Pasniedzeji p6 = new Pasniedzeji("Andris", "Liepa", "andris.liepa@tdl.com", "+37125555555");
				Pasniedzeji p7 = new Pasniedzeji("Eva", "Zariņa", "eva.zarina@tdl.com", "+37126666666");
				Pasniedzeji p8 = new Pasniedzeji("Mārtiņš", "Grīnbergs", "martins.grinbergs@tdl.com", "+37127777777");
				p1.setUser(u1);
				p2.setUser(u3);
				pasnRepo.save(p1);
				pasnRepo.save(p2);
				pasnRepo.save(p3);
				pasnRepo.save(p4);
				pasnRepo.save(p5);
				pasnRepo.save(p6);
				pasnRepo.save(p7);
				pasnRepo.save(p8);

				KursaDatumi kdat1 = new KursaDatumi(LocalDate.of(2025, 6, 15), LocalDate.of(2025, 6, 20), k1, p2);
				KursaDatumi kdat2 = new KursaDatumi(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 7, 10), k3, p2);
				KursaDatumi kdat3 = new KursaDatumi(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 7, 10), k5, p3);
				KursaDatumi kdat4 = new KursaDatumi(LocalDate.of(2025, 7, 1), LocalDate.of(2025, 7, 10), k2, p3);
				KursaDatumi kdat5 = new KursaDatumi(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 7, 10), k8, p3);
				KursaDatumi kdat6 = new KursaDatumi(LocalDate.of(2025, 6, 15), LocalDate.of(2025, 6, 20), k6, p1);
				KursaDatumi kdat7 = new KursaDatumi(LocalDate.of(2025, 6, 15), LocalDate.of(2025, 6, 20), k7, p1);

				kursaDatumiRepo.save(kdat1);
				kursaDatumiRepo.save(kdat2);
				kursaDatumiRepo.save(kdat3);
				kursaDatumiRepo.save(kdat4);
				kursaDatumiRepo.save(kdat5);
				kursaDatumiRepo.save(kdat6);
				kursaDatumiRepo.save(kdat7);

				Vertejumi v1 = new Vertejumi(8.7f, LocalDate.of(2025, 3, 15), kd1, kdat6);
				Vertejumi v2 = new Vertejumi(6.5f, LocalDate.of(2025, 4, 10), kd3, kdat7);
				Vertejumi v3 = new Vertejumi(9.2f, LocalDate.of(2025, 5, 5), kd5, kdat1);
				Vertejumi v4 = new Vertejumi(7.8f, LocalDate.of(2025, 6, 1), kd7, kdat2);
				Vertejumi v5 = new Vertejumi(3.2f, LocalDate.of(2025, 6, 12), kd2, kdat1);
				Vertejumi v6 = new Vertejumi(10.0f, LocalDate.of(2025, 7, 20), kd8, kdat2);
				Vertejumi v7 = new Vertejumi(9.5f, LocalDate.of(2025, 6, 12), kd4, kdat1);
				Vertejumi v8 = new Vertejumi(4.0f, LocalDate.of(2025, 5, 2), kd2, kdat2);
				vertejumiRepo.save(v1);
				vertejumiRepo.save(v2);
				vertejumiRepo.save(v3);
				vertejumiRepo.save(v4);
				vertejumiRepo.save(v5);
				vertejumiRepo.save(v6);
				vertejumiRepo.save(v7);
				vertejumiRepo.save(v8);

				MacibuRezultati mr1 = new MacibuRezultati("šeit ir aprakstīts sasniegtais mācību rezultāts", k1);
				macibuRezRepo.save(mr1);

				MacibuRezultati mr2 = new MacibuRezultati("šeit ir aprakstīts sasniegtais mācību rezultāts", k2);

				macibuRezRepo.save(mr2);
			}
		};
	}
}
