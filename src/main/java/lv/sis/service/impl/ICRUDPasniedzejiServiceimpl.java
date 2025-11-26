package lv.sis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lv.sis.model.Pasniedzeji;
import lv.sis.repo.IPasniedzejiRepo;
import lv.sis.service.ICRUDPasniedzejiService;
import lv.sis.service.IUserService;

@Service
public class ICRUDPasniedzejiServiceimpl implements ICRUDPasniedzejiService {
	@Autowired
    IPasniedzejiRepo pasnRepo;
	
	@Autowired
	IUserService userService;

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
	public Page<Pasniedzeji> retrieveAll(Pageable pageable) throws Exception {
		if (pasnRepo.count() == 0) {
			throw new Exception("Tabula ir tukša");
		}
		
		return pasnRepo.findAll(pageable);

	}

	@Override
	public Pasniedzeji retrieveById(int kdid) throws Exception {
		if (kdid < 0) {
			throw new Exception("ID nav pareizs");
		}
		if (!pasnRepo.existsById(kdid)) {
			throw new Exception("Sertifikats ar tadu id neeksistē");
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		int userID = userService.getUserIdFromUsername(auth.getName());
		
		for (GrantedAuthority a: auth.getAuthorities()) {
			if (a.getAuthority().equals("ADMIN")) {
				return pasnRepo.findById(kdid).get(); 
			}
		}
		
		Pasniedzeji pasniedzejs = pasnRepo.findById(kdid).get();
		if (pasniedzejs.getPid() == userID) {
			return pasniedzejs;
		}

		throw new Exception("This user does not have rights to watch this page."); 
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
