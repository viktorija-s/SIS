package lv.sis.service.impl;

import lv.sis.model.Kurss;
import lv.sis.repo.IKurssRepo;
import lv.sis.service.IFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class FilterServiceImpl implements IFilterService {

    @Autowired
    private IKurssRepo kurssRepo;

    @Override
    public ArrayList<Kurss> findByNosaukumsContainingIgnoreCase(String text) throws Exception {
        return kurssRepo.findByNosaukumsContaining(text);
    }



}
