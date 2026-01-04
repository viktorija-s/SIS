package lv.sis.service.impl;

import lv.sis.model.Kurss;
import lv.sis.model.Sertifikati;
import lv.sis.model.enums.CertificateType;
import lv.sis.repo.IKurssRepo;
import lv.sis.repo.ISertifikatiRepo;
import lv.sis.service.IFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class FilterServiceImpl implements IFilterService {

    @Autowired
    private IKurssRepo kurssRepo;
    
    @Autowired
    private ISertifikatiRepo sertifikatiRepo;

    @Override
    public ArrayList<Kurss> findByNosaukumsContainingIgnoreCase(String text) throws Exception {
        return kurssRepo.findByNosaukumsContaining(text);
    }

    @Override
    public ArrayList<Sertifikati> findByTipsContainingIgnoreCase(String text) throws Exception {
        CertificateType type = CertificateType.valueOf(text.toUpperCase());
        return sertifikatiRepo.findByTips(type);
    }



}
