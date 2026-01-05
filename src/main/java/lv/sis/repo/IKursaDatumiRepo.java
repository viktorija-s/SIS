package lv.sis.repo;

import org.springframework.data.repository.CrudRepository;

import lv.sis.model.KursaDatumi;

import java.time.LocalDate;
import java.util.ArrayList;

public interface IKursaDatumiRepo extends CrudRepository<KursaDatumi, Integer> {

        ArrayList<KursaDatumi> findBySakumaDatumsLessThanEqualAndBeiguDatumsGreaterThanEqual(LocalDate to, LocalDate from);
}
