package lv.sis.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.CrudRepository;

import lv.sis.model.KursaDatumi;

public interface IKursaDatumiRepo extends CrudRepository<KursaDatumi, Integer> {

	Page<KursaDatumi> findAll(Pageable pageable);

	Page<KursaDatumi> findAllByPasniedzejsPid(int pid, Pageable pageable);

}
