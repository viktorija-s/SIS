package lv.sis.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lv.sis.model.KursaDalibnieki;
import lv.sis.model.Kurss;
import lv.sis.model.Sertifikati;
import lv.sis.model.enums.CertificateType;
import lv.sis.repo.ICRUDKurssRepo;
import lv.sis.repo.IKursaDalibniekiRepo;
import lv.sis.repo.SertifikatiRepo;
import lv.sis.service.ICRUDSertifikatiService;

@Service
public class ICRUDSertifikatiServiceImpl implements ICRUDSertifikatiService {
	@Autowired
	private SertifikatiRepo sertRepo;
	@Autowired
	private IKursaDalibniekiRepo dalibniekiRepo; 
	@Autowired
	private ICRUDKurssRepo kurssRepo;

	@Override
	public void create(CertificateType tips, LocalDate izdosanasDatums, int regNr, boolean irParakstits,
			KursaDalibnieki dalibnieks, Kurss kurss) throws Exception {
		if (tips.equals(null) || izdosanasDatums.equals(null) || regNr < 0 || dalibnieks.equals(null) || kurss.equals(null)) {
			throw new Exception("Dati nav pareizi");
		}
		if (sertRepo.existsByRegistracijasNr(regNr)) {
			throw new Exception("Sertifikāts ar tādu reģistrācijas numuru jau eksistē");
		}
		
		Sertifikati newSert = new Sertifikati(tips, izdosanasDatums, regNr, irParakstits, dalibnieks, kurss);
		sertRepo.save(newSert);
	}

	@Override
	public Page<Sertifikati> retrieveAll(Pageable pageable) throws Exception {
		if (sertRepo.count() == 0) {
			throw new Exception("Tabula ir tukša");
		}
		
		return sertRepo.findAll(pageable);
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
	public void updateById(int id, CertificateType tips, LocalDate izdosanasDatums, int regNr, boolean irParakstits) throws Exception { 
		if (id < 0) {
			throw new Exception("ID nav pareizs");
		}
		if (!sertRepo.existsById(id)) {
			throw new Exception("Sertifikats ar tadu id neeksistē");
		}
		
		Sertifikati selectedSert = sertRepo.findById(id).get();
		
		selectedSert.setTips(tips);
		selectedSert.setIzdosanasDatums(izdosanasDatums);
		selectedSert.setRegistracijasNr(regNr);
		selectedSert.setIrParakstits(irParakstits);
		
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
		
		Sertifikati sert = sertRepo.findById(id).get();
		
		// atsaista sertifikatu citas klases
		ArrayList<KursaDalibnieki> dalibnieki = dalibniekiRepo.findByKdid(id);
		
		for (KursaDalibnieki dalibnieks: dalibnieki) {
			dalibnieks.setSertifikati(null);
			dalibniekiRepo.save(dalibnieks);
		}
		
		ArrayList<Kurss> kursi = kurssRepo.findByKid(id);
		for (Kurss kurss: kursi) {
			kurss.setSertifikati(null);
			kurssRepo.save(kurss);
		}
	
		
		sertRepo.delete(sert);
	}	
}
