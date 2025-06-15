package lv.sis.repo;

import org.springframework.data.repository.CrudRepository;

import lv.sis.model.Kurss;
import lv.sis.model.MacibuRezultati;

public interface IMacibuRezultatiRepo extends CrudRepository<MacibuRezultati, Integer>{

	boolean existsByMacibuRezultatsAndKurss(int macibuRezultats, Kurss kurss);

	MacibuRezultati findByMacibuRezultatsAndKurss(int macibuRezultats, Kurss kurss);	

}
