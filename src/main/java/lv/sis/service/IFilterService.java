package lv.sis.service;

import lv.sis.model.Kurss;
import lv.sis.model.Pasniedzeji;
import lv.sis.model.Vertejumi;


import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IFilterService {

    public abstract ArrayList<Kurss> findByNosaukumsContainingIgnoreCase(String text) throws Exception;
    
    Page<Vertejumi> retrieveByMinMax(Integer min, Integer max, Pageable pageable) throws Exception;
    
    Page<Pasniedzeji> retrieveByKurss(String nosaukums, Pageable pageable) throws Exception;


    }
