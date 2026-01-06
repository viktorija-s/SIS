package lv.sis.service;

import lv.sis.model.KursaDatumi;
import lv.sis.model.Kurss;
import lv.sis.model.Sertifikati;

import java.time.LocalDate;
import java.util.ArrayList;

public interface IFilterService {

    public abstract ArrayList<Kurss> findByNosaukumsContainingIgnoreCase(String text) throws Exception;

    ArrayList<KursaDatumi> findKursaDatumiBetweenDates(LocalDate from, LocalDate to) throws Exception;

    ArrayList<Sertifikati> findSertifikatiByDalibniekaVardsUzvards(String vards, String uzvards) throws Exception;
}
