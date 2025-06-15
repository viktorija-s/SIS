package lv.sis.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import lv.sis.model.Kurss;
import lv.sis.model.MacibuRezultati;
import lv.sis.repo.IMacibuRezultatiRepo;
import lv.sis.service.ICRUDMacibuRezultatiService;

public class CRUDMacibuRezultatiServiceImpl implements ICRUDMacibuRezultatiService{

	@Autowired
	private IMacibuRezultatiRepo macibuRezultatiRepo;
	
	@Override
	public void create(String macibuRezultats, Kurss kurss) throws Exception {
		if(macibuRezultats == null || kurss == null) {
			throw new Exception("Ievades parametri nav pareizi");
		}
		
		if (macibuRezultatiRepo.existsByMacibuRezultatsAndKurss(macibuRezultats, kurss)) {
			MacibuRezultati existingMacibuRezultats = macibuRezultatiRepo.findByMacibuRezultatsAndKurss(macibuRezultats, kurss);
		} else {
			MacibuRezultati newMacibuRezultats = new MacibuRezultati(macibuRezultats, kurss);
			macibuRezultatiRepo.save(newMacibuRezultats);
		}
		
	}

	@Override
	public ArrayList<MacibuRezultati> retrieveAll() throws Exception {
		if(macibuRezultatiRepo.count()==0) {
			throw new Exception("Tabulā nav neviena ieraksta");
		}
		ArrayList<MacibuRezultati> allMacibuRezultati = (ArrayList<MacibuRezultati>) macibuRezultatiRepo.findAll();
		return allMacibuRezultati;
	}

	@Override
	public MacibuRezultati retrieveById(int mrid) throws Exception {
		if(mrid < 0) {
			throw new Exception("Id nevar būt negatīvs");
		}
		
		if(!macibuRezultatiRepo.existsById(mrid)) {
			throw new Exception("Mācību rezultati ar tādu id neeksistē");
		}
		
		MacibuRezultati retrievedMacibuRezultati = macibuRezultatiRepo.findById(mrid).get();
		return retrievedMacibuRezultati;
	}

	@Override
	public void updateById(int mrid, String macibuRezultats, Kurss kurss) throws Exception {
		MacibuRezultati retrievedMacibuRezultati = retrieveById(mrid);
		
		if(retrievedMacibuRezultati.getMacibuRezultats() != macibuRezultats) {
			retrievedMacibuRezultati.setMacibuRezultats(macibuRezultats);
		}
		
		if(retrievedMacibuRezultati.getKurss() != kurss) {
			retrievedMacibuRezultati.setKurss(kurss);
		}
		
	}

	@Override
	public void deleteById(int mrid) throws Exception {
		if(mrid < 0) {
			throw new Exception("Id nevar būt negatīvs");
		}
		
		if(!macibuRezultatiRepo.existsById(mrid)) {
			throw new Exception("Mācību rezultāti ar tādu id neeksistē");
		}
		
		macibuRezultatiRepo.deleteById(mrid);;
		
	}
	

}
