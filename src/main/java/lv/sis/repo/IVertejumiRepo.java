package lv.sis.repo;

import org.springframework.data.repository.CrudRepository;

import lv.sis.model.KursaDalibnieki;
import lv.sis.model.KursaDatumi;
import lv.sis.model.Vertejumi;

public interface IVertejumiRepo extends CrudRepository<Vertejumi, Integer>{

	boolean existsByVertejumiAndDatums(float vertejumi, String datums, KursaDalibnieki kursaDalibnieki,
			KursaDatumi kursaDatumi);

	Vertejumi findByVertejumiAndDatums(float vertejumi, String datums, KursaDalibnieki kursaDalibnieki,
			KursaDatumi kursaDatumi);

}
