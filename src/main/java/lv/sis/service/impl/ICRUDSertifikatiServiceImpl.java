package lv.sis.service.impl;

import java.time.LocalDate;
import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lv.sis.model.KursaDalibnieki;
import lv.sis.model.Kurss;
import lv.sis.model.Sertifikati;
import lv.sis.model.enums.CertificateType;
import lv.sis.repo.IKurssRepo;
import lv.sis.repo.IKursaDalibniekiRepo;
import lv.sis.repo.ISertifikatiRepo;
import lv.sis.service.ICRUDSertifikatiService;

@Service
public class ICRUDSertifikatiServiceImpl implements ICRUDSertifikatiService {
	@Autowired
	private ISertifikatiRepo sertRepo;
	@Autowired
	private IKursaDalibniekiRepo dalibniekiRepo; 
	@Autowired
	private IKurssRepo kurssRepo;
	
	@Override
	public void create(CertificateType tips, LocalDate izdosanasDatums, int regNr, boolean irParakstits,
			KursaDalibnieki dalibnieks, Kurss kurss) throws Exception {
		if (tips == null || izdosanasDatums == null || regNr < 0 || dalibnieks == null || kurss == null) {
			throw new Exception("Dati nav pareizi");
		}
		if (sertRepo.existsByRegistracijasNr(regNr)) {
			throw new Exception("Sertifikāts ar tādu reģistrācijas numuru jau eksistē");
		}
		LocalDate minDate = LocalDate.of(2010, 1, 1);
		LocalDate now = LocalDate.now();
		if (izdosanasDatums.isBefore(minDate) || izdosanasDatums.isAfter(now)) {
			throw new Exception("Izdosanas datums nav pareizs: ir jabut starp " + minDate + " un " + now);
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
	public Page<Sertifikati> retrieveById(int id) throws Exception {
		if (id < 0) {
			throw new Exception("ID nav pareizs");
		}
		if (!sertRepo.existsById(id)) {
			throw new Exception("Sertifikats ar tadu id neeksistē");
		}
		
		Sertifikati sert = sertRepo.findById(id).get();
		Pageable pageable = PageRequest.of(0, 1);
		return new PageImpl<>(List.of(sert), pageable, 1);
	}

	@Override
	public void updateById(int id, CertificateType tips, LocalDate izdosanasDatums, int regNr, boolean irParakstits) throws Exception { 
		if (id < 0) {
			throw new Exception("ID nav pareizs");
		}
		if (!sertRepo.existsById(id)) {
			throw new Exception("Sertifikats ar tadu id neeksistē");
		}
		LocalDate minDate = LocalDate.of(2010, 1, 1);
		LocalDate now = LocalDate.now();
		if (izdosanasDatums.isBefore(minDate) || izdosanasDatums.isAfter(now)) {
			throw new Exception("Izdosanas datums nav pareizs: ir jabut starp " + minDate + " un " + now);
		}
		
		Sertifikati selectedSert = sertRepo.findById(id).get();
		
		if (selectedSert.isIrParakstits()) {
	        throw new Exception("Sertifikāts jau ir parakstīts un to nevar mainīt!");
	    }
		
		selectedSert.setTips(tips);
		
		
		selectedSert.setIzdosanasDatums(izdosanasDatums);
		selectedSert.setRegistracijasNr(regNr);
		selectedSert.setIrParakstits(irParakstits);
		
		sertRepo.save(selectedSert);
	}

    @Override
    @Transactional
    public void deleteById(int id) throws Exception {
        if (id < 0) {
            throw new Exception("ID nav pareizs");
        }
        if (!sertRepo.existsById(id)) {
            throw new Exception("Sertifikats ar tadu id neeksistē");
        }

        Sertifikati sert = sertRepo.findById(id).get();

        KursaDalibnieki dalibnieks = sert.getDalibnieks();
        if (dalibnieks != null) {
            dalibnieks.setSertifikati(null);
            dalibniekiRepo.save(dalibnieks);
        }

        Kurss kurss = sert.getKurss();
        if (kurss != null && kurss.getSertifikati() != null) {
            kurss.getSertifikati().remove(sert);
            kurssRepo.save(kurss);
        }

        sertRepo.delete(sert);
    }
}
