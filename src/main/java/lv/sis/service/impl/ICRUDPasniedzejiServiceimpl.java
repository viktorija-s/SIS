package lv.sis.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.sis.model.KursaDatumi;
import lv.sis.model.Pasniedzeji;
import lv.sis.repo.IKursaDatumiRepo;
import lv.sis.repo.IPasniedzejiRepo;
import lv.sis.service.ICRUDPasniedzejiService;

@Service
public class ICRUDPasniedzejiServiceimpl implements ICRUDPasniedzejiService {
	@Autowired
    IPasniedzejiRepo pasnRepo;
	
	@Autowired
	IKursaDatumiRepo kursaDatRepo;

	@Override
	public void create(String vards, String uzvards, String epasts, String telefonaNr) throws Exception {
		if (vards.equals(null) || uzvards.equals(null) || epasts.equals(null) || telefonaNr.equals(null)) {
			throw new Exception("Dati nav pareizi");
		}
		if (pasnRepo.existsByVardsAndUzvards(vards, uzvards) && pasnRepo.existsByEpasts(epasts)) {
			throw new Exception("Pasniedzējs ar e-pastu " + epasts + " jau eksistē");
		}
		if (pasnRepo.existsByTelefonaNr(telefonaNr)) {
			throw new Exception("Pasniedzējs ar numuru " + telefonaNr + " jau eksistē");
		}

		Pasniedzeji newSert = new Pasniedzeji(vards, uzvards, epasts, telefonaNr);
		pasnRepo.save(newSert);
	}

	@Override
	public ArrayList<Pasniedzeji> retrieveAll() throws Exception {
		// TODO Auto-generated method stub

		if (pasnRepo.count() == 0) {
			throw new Exception("Tabula ir tukša");
		}

		return (ArrayList<Pasniedzeji>) pasnRepo.findAll();
	}

	@Override
	public Pasniedzeji retrieveById(int kdid) throws Exception {
		// TODO Auto-generated method stub

		if (kdid < 0) {
			throw new Exception("ID nav pareizs");
		}
		if (!pasnRepo.existsById(kdid)) {
			throw new Exception("Sertifikats ar tadu id neeksistē");
		}

		return pasnRepo.findById(kdid).get();
	}

	@Override
	public void updateById(int kdid, String vards, String uzvards, String epasts, String telefonaNr) throws Exception {
		// TODO Auto-generated method stub
		if (kdid < 0) {
			throw new Exception("ID nav pareizs");
		}
		if (!pasnRepo.existsById(kdid)) {
			throw new Exception("Sertifikats ar tadu id neeksistē");
		}

		Pasniedzeji selectedSert = pasnRepo.findById(kdid).get();

		selectedSert.setVards(vards);
		selectedSert.setUzvards(uzvards);
		selectedSert.setEpasts(epasts);
		selectedSert.setTelefonaNr(telefonaNr);

		pasnRepo.save(selectedSert);

	}

	@Override
	public void deleteById(int pid) throws Exception {
		// TODO Auto-generated method stub
		if (pid < 0) {
			throw new Exception("Id nevar būt negatīvs");
		}

		if (!pasnRepo.existsById(pid)) {
			throw new Exception("Pasniedzējs ar tādu id neeksistē");
		}
		
		ArrayList<KursaDatumi> datumi = kursaDatRepo.findByPasniedzejsPid(pid);
        for (KursaDatumi kd: datumi) {
        	kd.setPasniedzejs(null);
        }

		pasnRepo.deleteById(pid);
	}

}
