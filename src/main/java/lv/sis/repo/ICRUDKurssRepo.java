package lv.sis.repo;

import org.springframework.data.repository.CrudRepository;

import lv.sis.model.Kurss;

public interface ICRUDKurssRepo extends CrudRepository<Kurss, Integer> {

	boolean existsByNosaukums(String nosaukums);

}
