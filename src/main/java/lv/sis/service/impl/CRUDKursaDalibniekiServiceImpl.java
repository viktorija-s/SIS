package lv.sis.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.sis.model.KursaDalibnieki;
import lv.sis.repo.IKursaDalibniekiRepo;
import lv.sis.service.ICRUDKursaDalibniekiService;

@Service
public class CRUDKursaDalibniekiServiceImpl implements ICRUDKursaDalibniekiService{
	
	@Autowired
	private IKursaDalibniekiRepo kursaDalibniekiRepo;
	
	@Override
	public void create(String vards, String uzvards, String epasts, String telefonaNr, String personasId, String pilseta, String valsts, String ielasNosaukumsNumurs, int dzivoklaNr, String pastaIndekss) throws Exception{
		if(vards == null || uzvards == null || epasts == null || telefonaNr == null || personasId == null || pilseta == null || valsts == null || ielasNosaukumsNumurs == null || dzivoklaNr < 0 || pastaIndekss == null) {
			throw new Exception("Ievades parametri nav pareizi");
		}
		
		if (kursaDalibniekiRepo.existsByVardsAndUzvards(vards, uzvards)) {
			KursaDalibnieki existingKursaDalibnieki = kursaDalibniekiRepo.findByVardsAndUzvards(vards, uzvards);
		} else {
			KursaDalibnieki newKursaDalibnieki = new KursaDalibnieki(vards, uzvards, epasts, telefonaNr, personasId, pilseta, valsts, ielasNosaukumsNumurs, dzivoklaNr, pastaIndekss);
			kursaDalibniekiRepo.save(newKursaDalibnieki);
		}
	}

	@Override
	public ArrayList<KursaDalibnieki> retrieveAll() throws Exception {
		if(kursaDalibniekiRepo.count()==0) {
			throw new Exception("Tabulā nav neviena ieraksta");
		}
		ArrayList<KursaDalibnieki> allKursaDalibnieki = (ArrayList<KursaDalibnieki>) kursaDalibniekiRepo.findAll();
		return allKursaDalibnieki;
	}

	@Override
	public KursaDalibnieki retrieveById(int kdid) throws Exception {
		if(kdid < 0) {
			throw new Exception("Id nevar būt negatīvs");
		}
		
		if(!kursaDalibniekiRepo.existsById(kdid)) {
			throw new Exception("Kursa dalībnieks ar tādu id neeksistē");
		}
		
		KursaDalibnieki retrievedKursaDalibnieki = kursaDalibniekiRepo.findById(kdid).get();
		return retrievedKursaDalibnieki;
	}

	@Override
	public void updateById(int kdid, String vards, String uzvards, String epasts, String telefonaNr, String personasId,
			String pilseta, String valsts, String ielasNosaukumsNumurs, int dzivoklaNr, String pastaIndekss)
			throws Exception {
		KursaDalibnieki retrievedKursaDalibnieki = retrieveById(kdid);
		
		if(retrievedKursaDalibnieki.getVards() != vards) {
			retrievedKursaDalibnieki.setVards(vards);
		}
		
		if(retrievedKursaDalibnieki.getUzvards() != uzvards) {
			retrievedKursaDalibnieki.setUzvards(uzvards);
		}
		
		if(retrievedKursaDalibnieki.getEpasts() != epasts) {
			retrievedKursaDalibnieki.setEpasts(epasts);
		}
		
		if(retrievedKursaDalibnieki.getTelefonaNr() != telefonaNr) {
			retrievedKursaDalibnieki.setTelefonaNr(telefonaNr);
		}
		
		if(retrievedKursaDalibnieki.getPersonasId() != personasId) {
			retrievedKursaDalibnieki.setPersonasId(personasId);
		}
		
		if(retrievedKursaDalibnieki.getPilseta() != pilseta) {
			retrievedKursaDalibnieki.setPilseta(pilseta);
		}
		
		if(retrievedKursaDalibnieki.getValsts() != valsts) {
			retrievedKursaDalibnieki.setValsts(valsts);
		}
		
		if(retrievedKursaDalibnieki.getIelasNosaukumsNumurs() != ielasNosaukumsNumurs) {
			retrievedKursaDalibnieki.setIelasNosaukumsNumurs(ielasNosaukumsNumurs);
		}
		
		if(retrievedKursaDalibnieki.getDzivoklaNr() != dzivoklaNr) {
			retrievedKursaDalibnieki.setDzivoklaNr(dzivoklaNr);
		}
		
		if(retrievedKursaDalibnieki.getPastaIndekss() != pastaIndekss) {
			retrievedKursaDalibnieki.setPastaIndekss(pastaIndekss);
		}
		
		kursaDalibniekiRepo.save(retrievedKursaDalibnieki);
	}

	@Override
	public void deleteById(int kdid) throws Exception {
		if(kdid < 0) {
			throw new Exception("Id nevar būt negatīvs");
		}
		
		if(!kursaDalibniekiRepo.existsById(kdid)) {
			throw new Exception("Kursa dalībnieks ar tādu id neeksistē");
		}
		
		kursaDalibniekiRepo.deleteById(kdid);;
		
	}

}
