package lv.sis.service.impl;

import java.sql.Date;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import lv.sis.model.KursaDalibnieki;
import lv.sis.model.Sertifikati;
import lv.sis.model.enums.CertificateType;
import lv.sis.repo.SertifikatiRepo;
import lv.sis.service.ICRUDSertifikatiService;

@Service
public class ICRUDSertifikatiServiceImpl implements ICRUDSertifikatiService {
	private SertifikatiRepo sertRepo;

	@Override
	public void create(CertificateType tips, Date izdosanasDatums, int regNr, boolean irParakstits,
			KursaDalibnieki dalibnieks) throws Exception {
		if (tips.equals(null) || izdosanasDatums.equals(null) || regNr < 0 || dalibnieks.equals(null)) {
			throw new Exception("Dati nav pareizi");
		}
		if (sertRepo.existsByRegNr(regNr)) {
			throw new Exception("Sertifikāts ar tādu reģistrācijas numuru jau eksistē");
		}
		
		Sertifikati newSert = new Sertifikati(tips, izdosanasDatums, regNr, irParakstits, dalibnieks);
		sertRepo.save(newSert);
	}

	@Override
	public ArrayList<Sertifikati> retrieveAll() throws Exception {
		if (sertRepo.count() == 0) {
			throw new Exception("Tabula ir tukša");
		}
		
		return (ArrayList<Sertifikati>)sertRepo.findAll();
	}

	@Override
	public Sertifikati retrieveById(int id) throws Exception {
		if (id < 0) {
			throw new Exception("ID nav pareizs");
		}
		if (!sertRepo.existsById(id)) {
			throw new Exception("Sertifikats ar tadu id neeksistē");
		}
		
		return sertRepo.findById(id).get();
	}

	@Override
	public void updateById(int id, boolean irParakstits, KursaDalibnieki dalibnieks) throws Exception {
		if (id < 0) {
			throw new Exception("ID nav pareizs");
		}
		if (!sertRepo.existsById(id)) {
			throw new Exception("Sertifikats ar tadu id neeksistē");
		}
		
		Sertifikati selectedSert = sertRepo.findById(id).get();
		
		selectedSert.setIrParakstits(irParakstits);
		selectedSert.setDalibnieks(dalibnieks);
		
		sertRepo.save(selectedSert);
	}

	@Override
	public void deleteById(int id) throws Exception {
		if (id < 0) {
			throw new Exception("ID nav pareizs");
		}
		if (!sertRepo.existsById(id)) {
			throw new Exception("Sertifikats ar tadu id neeksistē");
		}
		
		
	}	
}
