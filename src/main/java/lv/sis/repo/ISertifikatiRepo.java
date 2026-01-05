package lv.sis.repo;


import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import lv.sis.model.Sertifikati;

public interface ISertifikatiRepo extends CrudRepository<Sertifikati, Integer> {

    boolean existsByRegistracijasNr(int regNr);

    ArrayList<Sertifikati> findByKurssKid(int kid);

    ArrayList<Sertifikati> findByDalibnieks_VardsContainingIgnoreCaseAndDalibnieks_UzvardsContainingIgnoreCase(String vards, String uzvards);

}
