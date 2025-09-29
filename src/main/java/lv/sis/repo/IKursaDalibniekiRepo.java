package lv.sis.repo;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import lv.sis.model.KursaDalibnieki;

public interface IKursaDalibniekiRepo extends JpaRepository<KursaDalibnieki, Integer>{

	boolean existsByVardsAndUzvards(String vards, String uzvards);

	KursaDalibnieki findByVardsAndUzvards(String vards, String uzvards);
	
	ArrayList<KursaDalibnieki> findByKdid(int id);


}
