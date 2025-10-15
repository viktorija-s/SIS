package lv.sis.repo;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import lv.sis.model.Kurss;

public interface ICRUDKurssRepo extends JpaRepository<Kurss, Integer> {

	boolean existsByNosaukums(String nosaukums);

	ArrayList<Kurss> findByKid(int id);

}
