package lv.sis.service;

import lv.sis.model.Kurss;
import lv.sis.model.Sertifikati;

import java.util.ArrayList;

public interface IFilterService {

    public abstract ArrayList<Kurss> findByNosaukumsContainingIgnoreCase(String text) throws Exception;
    
    public abstract ArrayList<Sertifikati> findByTipsContainingIgnoreCase(String text) throws Exception;

    }
