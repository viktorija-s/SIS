package lv.sis.repo;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import lv.sis.model.Kurss;

public interface ICRUDKurssRepo extends CrudRepository<Kurss, Integer> {

	boolean existsByNosaukums(String nosaukums);

	ArrayList<Kurss> findByKid(int id);

}
