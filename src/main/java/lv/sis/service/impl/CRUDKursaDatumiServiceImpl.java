package lv.sis.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.sis.model.KursaDatumi;
import lv.sis.model.Kurss;
import lv.sis.model.Pasniedzeji;
import lv.sis.repo.KursaDatumiRepo;
import lv.sis.service.ICRUDKursaDatumiService;

@Service
public class CRUDKursaDatumiServiceImpl implements ICRUDKursaDatumiService {
	
	@Autowired
	private KursaDatumiRepo kursaDatumiRepo;

	@Override
	public void create(LocalDate sakumaDatums, LocalDate beiguDatums, Kurss kurss, Pasniedzeji pasniedzejs) throws Exception {	
		if (sakumaDatums == null || beiguDatums == null || kurss == null || pasniedzejs == null) {
            throw new Exception("Kļūda: visi lauki ir obligāti!");
        }

        if (beiguDatums.isBefore(sakumaDatums)) {
            throw new Exception("Beigu datums nevar būt pirms sākuma datuma!");
        } else { 
	        KursaDatumi kursaDatumi = new KursaDatumi(sakumaDatums, beiguDatums, kurss, pasniedzejs);
	        kursaDatumiRepo.save(kursaDatumi);
		
        }
	}

	@Override
	public ArrayList<KursaDatumi> retrieveAll() throws Exception {
		if (kursaDatumiRepo.count() == 0) {
            throw new Exception("Tabulā nav neviena kursa datumu ieraksta");
        }

        ArrayList<KursaDatumi> visiKursaDatumi = (ArrayList<KursaDatumi>) kursaDatumiRepo.findAll();
        return visiKursaDatumi;
	}

	@Override
	public KursaDatumi retrieveById(int kursaDatId) throws Exception {
		if (kursaDatId < 0) {
            throw new Exception("ID nevar būt negatīvs!");
        }

        if (!kursaDatumiRepo.existsById(kursaDatId)) {
            throw new Exception("Kursa datumi ar ID " + kursaDatId + " neeksistē!");
        }

        KursaDatumi kursaDatumi = kursaDatumiRepo.findById(kursaDatId).get();
        return kursaDatumi;
	}

	@Override
	public void updateById(int kursaDatId, KursaDatumi kursaDatumi) throws Exception {
		if (kursaDatId < 0) {
            throw new Exception("ID nevar būt negatīvs!");
        }

        if (!kursaDatumiRepo.existsById(kursaDatId)) {
            throw new Exception("Kursa datumi ar ID " + kursaDatId + " neeksistē!");
        }

        KursaDatumi retrievedKursaDatumi = kursaDatumiRepo.findById(kursaDatId).get();

        if (kursaDatumi.getSakumaDatums() != null && !kursaDatumi.getSakumaDatums().equals(retrievedKursaDatumi.getSakumaDatums())) {
        	retrievedKursaDatumi.setSakumaDatums(kursaDatumi.getSakumaDatums());
        }

        if (kursaDatumi.getBeiguDatums() != null && !kursaDatumi.getBeiguDatums().equals(retrievedKursaDatumi.getBeiguDatums())) {
        	retrievedKursaDatumi.setBeiguDatums(kursaDatumi.getBeiguDatums());
        }

        if (kursaDatumi.getKurss() != null && !kursaDatumi.getKurss().equals(retrievedKursaDatumi.getKurss())) {
        	retrievedKursaDatumi.setKurss(kursaDatumi.getKurss());
        }

        if (kursaDatumi.getPasniedzejs() != null && !kursaDatumi.getPasniedzejs().equals(retrievedKursaDatumi.getPasniedzejs())) {
        	retrievedKursaDatumi.setPasniedzejs(kursaDatumi.getPasniedzejs());
        }

        kursaDatumiRepo.save(retrievedKursaDatumi);
		
	}

	@Override
	public void deleteById(int kursaDatId) throws Exception {
		if (kursaDatId < 0) {
            throw new Exception("ID nevar būt negatīvs!");
        }

        if (!kursaDatumiRepo.existsById(kursaDatId)) {
            throw new Exception("Kursa datumi ar ID " + kursaDatId + " neeksistē!");
        }

        kursaDatumiRepo.deleteById(kursaDatId);
    
	}
	
}
