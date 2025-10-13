package lv.sis.repo;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import lv.sis.model.KursaDalibnieki;

public interface IKursaDalibniekiRepo extends CrudRepository<KursaDalibnieki, Integer>{

	boolean existsByVardsAndUzvards(String vards, String uzvards);
	boolean existsByPersonasId(String personasId);

	KursaDalibnieki findByVardsAndUzvards(String vards, String uzvards);
	
	ArrayList<KursaDalibnieki> findByKdid(int id);

	boolean existsByPersonasId(String personasId);

	Optional<KursaDalibnieki> findByPersonasId(String personasId);


    boolean existsByEpasts(String epasts);
}
