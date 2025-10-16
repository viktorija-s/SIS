package lv.sis.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.sis.model.Pasniedzeji;
import lv.sis.repo.IPasniedzejiRepo;
import lv.sis.service.ICRUDPasniedzejiService;

@Service
public class ICRUDPasniedzejiServiceimpl implements ICRUDPasniedzejiService {
	@Autowired
    IPasniedzejiRepo pasnRepo;

	@Override
	public void create(String vards, String uzvards, String epasts, String telefonaNr) throws Exception {
		if (vards == null || uzvards == null || epasts == null || telefonaNr == null) {
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
		if (pasnRepo.count() == 0) {
			throw new Exception("Tabula ir tukša");
		}

		return (ArrayList<Pasniedzeji>) pasnRepo.findAll();
	}

	@Override
	public Pasniedzeji retrieveById(int kdid) throws Exception {
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
	public void deleteById(int kdid) throws Exception {
		if (kdid < 0) {
			throw new Exception("Id nevar būt negatīvs");
		}

		if (!pasnRepo.existsById(kdid)) {
			throw new Exception("Pasniedzējs ar tādu id neeksistē");
		}

		pasnRepo.deleteById(kdid);
	}

}
