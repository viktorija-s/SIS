package lv.sis.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import lv.sis.model.Pasniedzeji;

public interface ICRUDPasniedzejiRepo extends JpaRepository<Pasniedzeji, Integer>{

	boolean existsByVardsAndUzvards(String vards, String uzvards);

}
