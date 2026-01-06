package lv.sis.service.impl;

import lv.sis.model.KursaDatumi;
import lv.sis.model.Kurss;
import lv.sis.model.Sertifikati;
import lv.sis.repo.IKursaDatumiRepo;
import lv.sis.repo.IKurssRepo;
import lv.sis.repo.ISertifikatiRepo;
import lv.sis.service.IFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
public class FilterServiceImpl implements IFilterService {

    @Autowired
    private IKurssRepo kurssRepo;

    @Autowired
    private IKursaDatumiRepo kursaDatumiRepo;

    @Autowired
    private ISertifikatiRepo sertifikatiRepo;

    @Override
    public ArrayList<Kurss> findByNosaukumsContainingIgnoreCase(String text) throws Exception {
        return kurssRepo.findByNosaukumsContaining(text);
    }

    @Override
    public ArrayList<KursaDatumi> findKursaDatumiBetweenDates(LocalDate from, LocalDate to) throws Exception {
        if (from == null || to == null || from.isAfter(to)) {
            throw new Exception("Nav norādīti pareizi datumi.");
        }
        return kursaDatumiRepo.findBySakumaDatumsLessThanEqualAndBeiguDatumsGreaterThanEqual(to, from);
    }

    @Override
    public ArrayList<Sertifikati> findSertifikatiByDalibniekaVardsUzvards(String vards, String uzvards) throws Exception {
        if ((vards == null || vards.isBlank()) &&
                (uzvards == null || uzvards.isBlank())) {
            throw new Exception("Name or surname must be provided");
        }
        return sertifikatiRepo.findByDalibnieks_VardsContainingIgnoreCaseAndDalibnieks_UzvardsContainingIgnoreCase(vards == null ? "" : vards.trim(), uzvards == null ? "" : uzvards.trim());
    }


}
