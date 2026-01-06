package lv.sis.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.CrudRepository;

import lv.sis.model.KursaDatumi;

import java.time.LocalDate;
import java.util.ArrayList;

public interface IKursaDatumiRepo extends CrudRepository<KursaDatumi, Integer> {

    ArrayList<KursaDatumi> findBySakumaDatumsLessThanEqualAndBeiguDatumsGreaterThanEqual(LocalDate to, LocalDate from);

    Page<KursaDatumi> findAll(Pageable pageable);

    Page<KursaDatumi> findAllByPasniedzejsPid(int pid, Pageable pageable);

}
