package lv.sis.repo;

import org.springframework.data.repository.CrudRepository;

import lv.sis.model.KursaDalibnieki;

public interface IKursaDalibniekiRepo extends CrudRepository<KursaDalibnieki, Integer>{

	boolean existsByVardsAndUzvards(String vards, String uzvards, String epasts, String telefonaNr, String personasId,
			String pilseta, String valsts, String ielasNosaukumsNumurs, int dzivoklaNr, String pastaIndekss);

	KursaDalibnieki findByVardsAndUzvards(String vards, String uzvards, String epasts, String telefonaNr,
			String personasId, String pilseta, String valsts, String ielasNosaukumsNumurs, int dzivoklaNr,
			String pastaIndekss);

}
