package lv.sis.repo;

import org.springframework.data.repository.CrudRepository;
import lv.sis.model.Pasniedzeji;

public interface ICRUDPasniedzejiRepo extends CrudRepository<Pasniedzeji, Integer>{

	boolean existsByTelefonaNr(String telefonaNr);

	boolean existsByVardsAndUzvards(String vards, String uzvards);

	boolean existsByEpasts(String epasts);

}
