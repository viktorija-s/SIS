package lv.sis.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.sis.model.Pasniedzeji;
import lv.sis.repo.ICRUDPasniedzejiRepo;
import lv.sis.service.ICRUDPasniedzejiService;

@Service
public class ICRUDPasniedzejiServiceimpl implements ICRUDPasniedzejiService {
	@Autowired
	ICRUDPasniedzejiRepo pasnRepo;

	@Override
	public void create(String vards, String uzvards, String epasts, String telefonaNr) throws Exception {
		// TODO Auto-generated method stub
		if (vards.equals(null) || uzvards.equals(null) || epasts.equals(null) || telefonaNr.equals(null)) {
			throw new Exception("Dati nav pareizi");
		}
		if (pasnRepo.existsByVardsAndUzvards(vards, uzvards)) {
			throw new Exception("Tads pasniedzējs jau eksistē");
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
		
		return (ArrayList<Pasniedzeji>)pasnRepo.findAll();
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
		selectedSert.setTelnummurs(telefonaNr); 
		
		pasnRepo.save(selectedSert);
		
	}

	@Override
	public void deleteById(int kdid) throws Exception { 
		// TODO Auto-generated method stub
		if(kdid < 0) {
			throw new Exception("Id nevar būt negatīvs");
		}
		
		if(!pasnRepo.existsById(kdid)) {
			throw new Exception("Pasniedzējs ar tādu id neeksistē");
		}
		
		pasnRepo.deleteById(kdid);
	}

} 
