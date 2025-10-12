package lv.sis.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.sis.model.Kurss;
import lv.sis.model.Sertifikati;
import lv.sis.model.enums.Limeni;
import lv.sis.repo.ICRUDKurssRepo;
import lv.sis.repo.SertifikatiRepo;
import lv.sis.service.ICRUDKurssService;

@Service
public class ICRUDKurssServiceImpl implements ICRUDKurssService{
	@Autowired
	ICRUDKurssRepo kurssRepo;

	@Override
	public void create(String nosaukums, int stundas, Limeni limenis) throws Exception {
		// TODO Auto-generated method stub
		if (nosaukums == null || stundas<0 || limenis == null) {
			throw new Exception("Dati nav pareizi");
		}
		if (kurssRepo.existsByNosaukums(nosaukums)) {
			throw new Exception("Tads kurss jau eksistē");
		}
		
		Kurss newKurss = new Kurss(nosaukums, stundas, limenis);
		kurssRepo.save(newKurss);
	
	}

	@Override
	public ArrayList<Kurss> retrieveAll() throws Exception {
		// TODO Auto-generated method stub
			if (kurssRepo.count() == 0) {
			throw new Exception("Tabula ir tukša");
		}
		
		return (ArrayList<Kurss>)kurssRepo.findAll();
	
	}

	@Override
	public Kurss retrieveById(int kdid) throws Exception {
		// TODO Auto-generated method stub

		if (kdid < 0) {
			throw new Exception("ID nav pareizs");
		}
		if (!kurssRepo.existsById(kdid)) {
			throw new Exception("Kurss ar tadu id neeksistē");
		}
		
		return kurssRepo.findById(kdid).get();
	}

	@Override
	public void updateById(int kdid, String nosaukums, int stundas, Limeni limenis) throws Exception {
		// TODO Auto-generated method stub
		if (kdid < 0) {
			throw new Exception("ID nav pareizs");
		}
		if (!kurssRepo.existsById(kdid)) {
			throw new Exception("Kurss ar tadu id neeksistē");
		}
		
		Kurss selectedKurss = kurssRepo.findById(kdid).get();
		
		selectedKurss.setNosaukums(nosaukums);
		selectedKurss.setStundas(stundas);
		selectedKurss.setLimenis(limenis);
		
		kurssRepo.save(selectedKurss);
	}

	@Override
    public void deleteById(int kid) throws Exception {
        if (kid < 0) {
            throw new Exception("Id nevar būt negatīvs");
        }
        if (!kurssRepo.existsById(kid)) {
            throw new Exception("Kurss ar tādu ID neeksistē");
        }
        kurssRepo.deleteById(kid);
    }


}
