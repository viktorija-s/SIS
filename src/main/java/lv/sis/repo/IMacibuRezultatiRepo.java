package lv.sis.repo;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import lv.sis.model.Kurss;
import lv.sis.model.MacibuRezultati;

public interface IMacibuRezultatiRepo extends CrudRepository<MacibuRezultati, Integer>{

	boolean existsByMacibuRezultatsAndKurss(String macibuRezultats, Kurss kurss);

	MacibuRezultati findByMacibuRezultatsAndKurss(String macibuRezultats, Kurss kurss);

	ArrayList<MacibuRezultati> findByKurssKid(int kid);	

}
