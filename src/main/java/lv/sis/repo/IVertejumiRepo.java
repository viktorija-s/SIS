package lv.sis.repo;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import lv.sis.model.Vertejumi;

public interface IVertejumiRepo extends JpaRepository<Vertejumi, Integer>{

	boolean existsByVertejumsAndDatums(float vertejumi, LocalDate datums);

	Vertejumi findByVertejumsAndDatums(float vertejumi, LocalDate datums);
	
	Vertejumi findByKursaDalibnieki_KdidAndKursaDatumi_Kurss_Kid(int kursaDalibnieksId, int kurssId);


}
