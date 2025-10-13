package lv.sis.service;

import lv.sis.model.Kurss;

import java.util.ArrayList;

public interface IFilterService {

    public abstract ArrayList<Kurss> findByNosaukumsContainingIgnoreCase(String text) throws Exception;

    }
