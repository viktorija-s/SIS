package lv.sis.repo;


import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import lv.sis.model.Sertifikati;
import lv.sis.model.enums.CertificateType;

public interface ISertifikatiRepo extends JpaRepository<Sertifikati, Integer> {


	ArrayList<Sertifikati> findByKurssKid(int kid);
	
	boolean existsByCertificateNo(String certificateNo);

	@Query("SELECT s.certificateNo FROM Sertifikati s")
	ArrayList<String> findAllCertificateNumbers();

	ArrayList<Sertifikati> findByTips(CertificateType tips);

}