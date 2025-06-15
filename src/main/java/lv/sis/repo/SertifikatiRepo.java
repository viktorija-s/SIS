package lv.sis.repo;

import org.springframework.data.repository.CrudRepository;

import lv.sis.model.Sertifikati;

public interface SertifikatiRepo extends CrudRepository<Sertifikati, Integer> {

	boolean existsByRegistracijasNr(int regNr);

}
