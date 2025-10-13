package lv.sis.repo;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import lv.sis.model.KursaDatumi;

public interface IKursaDatumiRepo extends CrudRepository<KursaDatumi, Integer> {

	ArrayList<KursaDatumi> findByKurssKid(int kid);

	ArrayList<KursaDatumi> findByPasniedzejsPid(int pid);

}
