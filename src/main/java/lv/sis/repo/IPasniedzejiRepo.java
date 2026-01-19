package lv.sis.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import lv.sis.model.Pasniedzeji;


public interface IPasniedzejiRepo extends JpaRepository<Pasniedzeji, Integer>{

	boolean existsByTelefonaNr(String telefonaNr);

	boolean existsByVardsAndUzvards(String vards, String uzvards);

	boolean existsByEpasts(String epasts);
	
	Pasniedzeji findByUserUsername(String username);
	
	Page<Pasniedzeji> findByKursaDatumi_Kurss_NosaukumsContainingIgnoreCase(String nosaukums, Pageable pageable);

}
