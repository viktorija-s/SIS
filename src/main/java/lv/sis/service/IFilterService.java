package lv.sis.service;

import lv.sis.model.*;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IFilterService {

    
    public abstract ArrayList<Sertifikati> findByTipsContainingIgnoreCase(String text) throws Exception;

    Page<Vertejumi> retrieveByMinMax(Integer min, Integer max, Pageable pageable) throws Exception;
    
    Page<Pasniedzeji> retrieveByKurss(String nosaukums, Pageable pageable) throws Exception;

    Page<KursaDatumi> findKursaDatumiBetweenDates(LocalDate from, LocalDate to) throws Exception;

    ArrayList<Sertifikati> findSertifikatiByDalibniekaVardsUzvards(String vards, String uzvards) throws Exception;

	Page<Kurss> findByNosaukumsContainingIgnoreCase(String text) throws Exception;

    Page<KursaDalibnieki> findDalibniekiByNameQuery(String q, Pageable pageable);
}
