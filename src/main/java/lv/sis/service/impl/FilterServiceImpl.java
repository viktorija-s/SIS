package lv.sis.service.impl;

import lv.sis.model.Kurss;
import lv.sis.model.Pasniedzeji;
import lv.sis.model.Vertejumi;
import lv.sis.repo.IKurssRepo;
import lv.sis.repo.IPasniedzejiRepo;
import lv.sis.repo.IVertejumiRepo;
import lv.sis.service.IFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.ArrayList;

@Service
public class FilterServiceImpl implements IFilterService {

    @Autowired
    private IKurssRepo kurssRepo;
    
    @Autowired IVertejumiRepo vertejumiRepo;
    
    @Autowired IPasniedzejiRepo pasniedzejiRepo;

    @Override
    public ArrayList<Kurss> findByNosaukumsContainingIgnoreCase(String text) throws Exception {
        return kurssRepo.findByNosaukumsContaining(text);
    }
    
    @Override
    public Page<Vertejumi> retrieveByMinMax(Integer min, Integer max, Pageable pageable) throws Exception {

        if (min == null && max == null) {
            throw new Exception("Jānorāda MIN vai MAX vērtējums");
        }

        int minValue = (min == null) ? 0 : min;
        int maxValue = (max == null) ? 10 : max;

        if (minValue < 0 || maxValue > 10) {
            throw new Exception("Vērtējumam jābūt robežās no 0 līdz 10");
        }

        if (minValue > maxValue) {
            throw new Exception("MIN vērtējums nedrīkst būt lielāks par MAX");
        }

        return vertejumiRepo.findByVertejumsBetween(minValue, maxValue, pageable);
    }
    
    @Override
    public Page<Pasniedzeji> retrieveByKurss(String nosaukums, Pageable pageable) throws Exception {
        
        if (nosaukums == null || nosaukums.trim().isEmpty()) {
            throw new Exception("Jānorāda kursa nosaukums");
        }
        
       
        return pasniedzejiRepo.findByKursaDatumi_Kurss_NosaukumsContainingIgnoreCase(nosaukums, pageable);
    }



}
