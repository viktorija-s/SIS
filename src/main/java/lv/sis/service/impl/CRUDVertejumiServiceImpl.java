package lv.sis.service.impl;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lv.sis.model.KursaDalibnieki;
import lv.sis.model.KursaDatumi;
import lv.sis.model.Vertejumi;
import lv.sis.repo.IVertejumiRepo;
import lv.sis.service.ICRUDVertejumiService;

@Service
public class CRUDVertejumiServiceImpl implements ICRUDVertejumiService {

	@Autowired
	private IVertejumiRepo vertejumiRepo;

	@Override
	public void create(float vertejumi, LocalDate datums, KursaDalibnieki kursaDalibnieki, KursaDatumi kursaDatumi)
			throws Exception {
		if (vertejumi < 0 || datums == null || kursaDalibnieki == null || kursaDatumi == null) {
			throw new Exception("Ievades parametri nav pareizi");
		}

		if (!vertejumiRepo.existsByVertejumsAndDatums(vertejumi, datums) && !datums.isBefore(LocalDate.now())) {
			Vertejumi newVertejumi = new Vertejumi(vertejumi, datums, kursaDalibnieki, kursaDatumi);
			vertejumiRepo.save(newVertejumi);
		} else {
			throw new Exception("Šāds vērtējums jau eksistē vai ir norādīts nepareizs datums.");
		}
		// TODO varbūt nomainīt uz to ka tieši pretēji ka nevar būt nākotnes datums un
		// pagātnes datums var būt tikai noteiktu laiku atpakaļ, piemēram, tikai
		// mēnesis.
	}

	@Override
	public Page<Vertejumi> retrieveAll(Pageable pageable) throws Exception {
		if(vertejumiRepo.count()==0) {

			throw new Exception("Tabulā nav neviena ieraksta");
		}
		
		Page<Vertejumi> allVertejumi = vertejumiRepo.findAll(pageable);
		return allVertejumi;
	}

	@Override
	public Vertejumi retrieveById(int vid) throws Exception {
		if (vid < 0) {
			throw new Exception("Id nevar būt negatīvs");
		}

		if (!vertejumiRepo.existsById(vid)) {
			throw new Exception("Vērtējumi ar tādu id neeksistē");
		}

		Vertejumi retrievedVertejumi = vertejumiRepo.findById(vid).get();
		return retrievedVertejumi;
	}

	@Override
	public void updateById(int vid, float vertejums) throws Exception {
		Vertejumi retrievedVertejumi = retrieveById(vid);

		if (retrievedVertejumi.getVertejums() != vertejums) {
			retrievedVertejumi.setVertejums(vertejums);
		}

		vertejumiRepo.save(retrievedVertejumi);
	}

	@Override
	public void deleteById(int vid) throws Exception {
		if (vid < 0) {
			throw new Exception("Id nevar būt negatīvs");
		}

		if (!vertejumiRepo.existsById(vid)) {
			throw new Exception("Vērtējumi ar tādu id neeksistē");
		}

		vertejumiRepo.deleteById(vid);
		;

	}

}
