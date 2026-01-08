package lv.sis.repo;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import lv.sis.model.Sertifikati;
import lv.sis.model.enums.CertificateType;

public interface ISertifikatiRepo extends JpaRepository<Sertifikati, Integer> {

	boolean existsByCertificateNo(String certificateNo);

	ArrayList<Sertifikati> findByKurssKid(int kid);

	ArrayList<Sertifikati> findByDalibnieks_VardsContainingIgnoreCaseAndDalibnieks_UzvardsContainingIgnoreCase(
			String vards, String uzvards);

	@Query("SELECT s.certificateNo FROM Sertifikati s")
	ArrayList<String> findAllCertificateNumbers();

	ArrayList<Sertifikati> findByTips(CertificateType tips);
}
