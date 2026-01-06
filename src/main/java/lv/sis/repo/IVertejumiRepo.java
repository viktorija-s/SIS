package lv.sis.repo;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import lv.sis.model.Vertejumi;

public interface IVertejumiRepo extends JpaRepository<Vertejumi, Integer>{

	boolean existsByVertejumsAndDatums(float vertejumi, LocalDate datums);

	Vertejumi findByVertejumsAndDatums(float vertejumi, LocalDate datums);
	
	Vertejumi findByKursaDalibnieki_KdidAndKursaDatumi_Kurss_Kid(int kursaDalibnieksId, int kurssId);

	Page<Vertejumi> findAllByKursaDatumiPasniedzejsPid(int i, Pageable pageable);

	@Query("SELECT AVG(v.vertejums) FROM Vertejumi v WHERE v.kursaDalibnieki.kdid = :kdid")
	Float findAvgGrade(int kdid);

}
