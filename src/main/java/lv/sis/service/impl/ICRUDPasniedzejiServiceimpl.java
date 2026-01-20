package lv.sis.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
	
    private final IPasniedzejiRepo pasnRepo;
	
	public ICRUDPasniedzejiServiceimpl(IPasniedzejiRepo pasnRepo, IUserService userService) {
		this.pasnRepo = pasnRepo;
	}

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
	public Page<Pasniedzeji> retrieveById(int kdid) throws Exception {
		if (kdid < 0) {
			throw new Exception("ID nav pareizs");
		}
		if (!pasnRepo.existsById(kdid)) {
			throw new Exception("Sertifikats ar tadu id neeksistē");
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		
		for (GrantedAuthority a: auth.getAuthorities()) {
			if (a.getAuthority().equals("ADMIN")) {
				Optional<Pasniedzeji> opt = pasnRepo.findById(kdid);
				if (opt.isEmpty()) throw new Exception("Pasniedzējs neeksistē");
				Pasniedzeji pasn = opt.get(); 
				Pageable pageable = PageRequest.of(0, 1);
				return new PageImpl<>(List.of(pasn), pageable, 1);
			}
		}
		
		Pasniedzeji pasniedzejs = pasnRepo.findByUserUsername(username);
		if (pasniedzejs == null) {
		    throw new Exception("Šim lietotājam nav piesaistīts pasniedzējs");
		}
		if (pasniedzejs.getPid() == kdid) {
			Pageable pageable = PageRequest.of(0, 1);
			return new PageImpl<>(List.of(pasniedzejs), pageable, 1);
		}

		throw new Exception("This user does not have rights to watch this page."); 
	}

	@Override
	public void updateById(int kdid, String vards, String uzvards, String epasts, String telefonaNr) throws Exception {
		if (kdid < 0) {
			throw new Exception("ID nav pareizs");
		}
		if (!pasnRepo.existsById(kdid)) {
			throw new Exception("Pasniedzējs ar tadu id neeksistē");
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		Pasniedzeji pasn;
		
		for (GrantedAuthority a: auth.getAuthorities()) {
			if (a.getAuthority().equals("ADMIN")) {
				Optional<Pasniedzeji> opt = pasnRepo.findById(kdid);
				if (opt.isEmpty()) throw new Exception("Pasniedzējs neeksistē");
				pasn = opt.get(); 
			}
		}
		
		pasn = pasnRepo.findByUserUsername(username);
		if (pasn == null) {
		    throw new Exception("Šim lietotājam nav piesaistīts pasniedzējs");
		}

		pasn.setVards(vards);
		pasn.setUzvards(uzvards);
		pasn.setEpasts(epasts);
		pasn.setTelefonaNr(telefonaNr);

		pasnRepo.save(pasn);
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
