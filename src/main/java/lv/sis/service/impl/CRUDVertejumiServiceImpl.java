package lv.sis.service.impl;

import java.sql.Date;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import lv.sis.model.KursaDalibnieki;
import lv.sis.model.KursaDatumi;
import lv.sis.model.Vertejumi;
import lv.sis.repo.IVertejumiRepo;
import lv.sis.service.ICRUDVertejumiService;

public class CRUDVertejumiServiceImpl implements ICRUDVertejumiService{

	@Autowired
	private IVertejumiRepo vertejumiRepo;
	
	@Override
	public void create(float vertejumi, Date datums, KursaDalibnieki kursaDalibnieki, KursaDatumi kursaDatumi)
			throws Exception {
		if(vertejumi < 0 || datums == null || kursaDalibnieki == null || kursaDatumi == null) {
			throw new Exception("Ievades parametri nav pareizi");
		}
		
		if (vertejumiRepo.existsByVertejumiAndDatums(vertejumi, datums, kursaDalibnieki, kursaDatumi)) {
			Vertejumi existingVertejumi = vertejumiRepo.findByVertejumiAndDatums(vertejumi, datums, kursaDalibnieki, kursaDatumi);
		} else {
			Vertejumi newVertejumi = new Vertejumi(vertejumi, datums, kursaDalibnieki, kursaDatumi);
			vertejumiRepo.save(newVertejumi);
		}
		
	}

	@Override
	public ArrayList<Vertejumi> retrieveAll() throws Exception {
		if(vertejumiRepo.count()==0) {
			throw new Exception("Tabulā nav neviena ieraksta");
		}
		ArrayList<Vertejumi> allVertejumi = (ArrayList<Vertejumi>) vertejumiRepo.findAll();
		return allVertejumi;
	}

	@Override
	public Vertejumi retrieveById(int vid) throws Exception {
		if(vid < 0) {
			throw new Exception("Id nevar būt negatīvs");
		}
		
		if(!vertejumiRepo.existsById(vid)) {
			throw new Exception("Vērtējumi ar tādu id neeksistē");
		}
		
		Vertejumi retrievedVertejumi = vertejumiRepo.findById(vid).get();
		return retrievedVertejumi;
	}

	@Override
	public void updateById(int vid, float vertejums) throws Exception {
		Vertejumi retrievedVertejumi = retrieveById(vid);
		
		if(retrievedVertejumi.getVertejums() != vertejums) {
			retrievedVertejumi.setVertejums(vertejums);
		}
		
	}

	@Override
	public void deleteById(int vid) throws Exception {
		if(vid < 0) {
			throw new Exception("Id nevar būt negatīvs");
		}
		
		if(!vertejumiRepo.existsById(vid)) {
			throw new Exception("Vērtējumi ar tādu id neeksistē");
		}
		
		vertejumiRepo.deleteById(vid);;
		
	}

}
