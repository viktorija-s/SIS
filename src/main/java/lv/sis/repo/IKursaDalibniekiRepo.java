package lv.sis.repo;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import lv.sis.model.KursaDalibnieki;

public interface IKursaDalibniekiRepo extends JpaRepository<KursaDalibnieki, Integer>{

	boolean existsByVardsAndUzvards(String vards, String uzvards);
	
	boolean existsByPersonasId(String personasId);

	KursaDalibnieki findByVardsAndUzvards(String vards, String uzvards);
	
	ArrayList<KursaDalibnieki> findByKdid(int id);

	Optional<KursaDalibnieki> findByPersonasId(String personasId);

    boolean existsByEpasts(String epasts);

    Page<KursaDalibnieki> findByVardsContainingIgnoreCaseOrUzvardsContainingIgnoreCase(String trimmed, String trimmed1, Pageable pageable);
}
