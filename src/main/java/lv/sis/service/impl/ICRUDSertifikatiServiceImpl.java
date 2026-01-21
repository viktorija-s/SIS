package lv.sis.service.impl;

import java.time.LocalDate;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ICRUDSertifikatiServiceImpl implements ICRUDSertifikatiService {

    private static final Logger logger = LoggerFactory.getLogger(ICRUDSertifikatiServiceImpl.class);

    @Autowired
    private ISertifikatiRepo sertRepo;
    @Autowired
    private IKursaDalibniekiRepo dalibniekiRepo;
    @Autowired
    private IKurssRepo kurssRepo;

    @Override
    public void create(CertificateType tips, LocalDate izdosanasDatums, int regNr, boolean irParakstits,
                       KursaDalibnieki dalibnieks, Kurss kurss) throws Exception {

        logger.info("Creating certificate");
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
        logger.info("Certificate created successfully");
    }

    @Override
    public Page<Sertifikati> retrieveAll(Pageable pageable) throws Exception {
        logger.info("Retrieving certificates");
        if (sertRepo.count() == 0) {
            throw new Exception("Tabula ir tukša");
        }
        return sertRepo.findAll(pageable);
    }

    @Override
    public Sertifikati retrieveById(int id) throws Exception {
        logger.info("Retrieving certificate id={}", id);
        if (id < 0) {
            throw new Exception("ID nav pareizs");
        }
        if (!sertRepo.existsById(id)) {
            throw new Exception("Sertifikats ar tadu id neeksistē");
        }
        return sertRepo.findById(id).get();
    }

    @Override
    public void updateById(int id, CertificateType tips, LocalDate izdosanasDatums, int regNr, boolean irParakstits)
            throws Exception {

        logger.info("Updating certificate id={}", id);
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
        logger.info("Certificate updated id={}", id);
    }

    @Override
    @Transactional
    public void deleteById(int id) throws Exception {
        logger.warn("Deleting certificate id={}", id);
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
        logger.info("Certificate deleted id={}", id);
    }
}

