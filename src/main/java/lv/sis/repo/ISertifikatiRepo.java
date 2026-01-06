package lv.sis.repo;


import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import lv.sis.model.Sertifikati;

public interface ISertifikatiRepo extends JpaRepository<Sertifikati, Integer> {

    boolean existsByRegistracijasNr(int regNr);

    ArrayList<Sertifikati> findByKurssKid(int kid);

    ArrayList<Sertifikati> findByDalibnieks_VardsContainingIgnoreCaseAndDalibnieks_UzvardsContainingIgnoreCase(String vards, String uzvards);

}
