package lv.sis.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lv.sis.model.KursaDatumi;
import lv.sis.model.Kurss;
import lv.sis.model.MacibuRezultati;
import lv.sis.model.Pasniedzeji;
import lv.sis.model.Sertifikati;
import lv.sis.model.enums.Limeni;
import lv.sis.repo.ICRUDKurssRepo;
import lv.sis.repo.IMacibuRezultatiRepo;
import lv.sis.repo.IPasniedzejiRepo;
import lv.sis.repo.ISertifikatiRepo;

import lv.sis.service.ICRUDKurssService;
import lv.sis.service.IUserService;

@Service
public class ICRUDKurssServiceImpl implements ICRUDKurssService{
	@Autowired
	ICRUDKurssRepo kurssRepo;
	
	@Autowired
	IMacibuRezultatiRepo macRezRepo;
	
	@Autowired
	ISertifikatiRepo sertRepo;
	
	@Autowired
	IPasniedzejiRepo pasnRepo;
	
	@Autowired
	IUserService userService;
	

	@Override
	public void create(String nosaukums, int stundas, Limeni limenis) throws Exception {
		if (nosaukums == null || stundas<0 || limenis == null) {
			throw new Exception("Dati nav pareizi");
		}
		if (kurssRepo.existsByNosaukums(nosaukums)) {
			throw new Exception("Tads kurss jau eksistē");
		}
		
		Kurss newKurss = new Kurss(nosaukums, stundas, limenis);
		kurssRepo.save(newKurss);
	}

	@Override
	public Page<Kurss> retrieveAll(Pageable pageable) throws Exception {
		if (kurssRepo.count() == 0) {
			throw new Exception("Tabula ir tukša");
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		
		for (GrantedAuthority a: auth.getAuthorities()) {
			if (a.getAuthority().equals("ADMIN")) {
				return kurssRepo.findAll(pageable); 
			}
		}
		
		Pasniedzeji professor = pasnRepo.findByUserUsername(username);
		return kurssRepo.findAllByKursaDatumiPasniedzejsPid(professor.getPid(), pageable);
	
	}

	@Override
	public Kurss retrieveById(int kdid) throws Exception {
		if (kdid < 0) {
			throw new Exception("ID nav pareizs");
		}
		if (!kurssRepo.existsById(kdid)) {
			throw new Exception("Kurss ar tadu id neeksistē");
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		
		for (GrantedAuthority a: auth.getAuthorities()) {
			if (a.getAuthority().equals("ADMIN")) {
				return kurssRepo.findById(kdid).get(); 
			}
		}
		
		Pasniedzeji pasn = pasnRepo.findByUserUsername(username);
		Kurss course = kurssRepo.findById(kdid).get();
		for (KursaDatumi kd : course.getKursaDatumi()) {
			if (pasn.getPid() == kd.getPasniedzejs().getPid()) {
				return course;
			}
		}
		
		throw new Exception("This user does not have rights to watch this page."); 
	}

	@Override
	public void updateById(int kdid, String nosaukums, int stundas, Limeni limenis) throws Exception {
		if (kdid < 0) {
			throw new Exception("ID nav pareizs");
		}
		if (!kurssRepo.existsById(kdid)) {
			throw new Exception("Kurss ar tadu id neeksistē");
		}
		
		Kurss selectedKurss = kurssRepo.findById(kdid).get();
		
		selectedKurss.setNosaukums(nosaukums);
		selectedKurss.setStundas(stundas);
		selectedKurss.setLimenis(limenis);
		
		kurssRepo.save(selectedKurss);
	}

	@Override
	public void deleteById(int kid) throws Exception {
		if(kid < 0) {
			throw new Exception("Id nevar būt negatīvs");
		}
		
		if(!kurssRepo.existsById(kid)) {
			throw new Exception("Kurss ar tādu id neeksistē");
		}
		
		ArrayList<MacibuRezultati> macRez = macRezRepo.findByKurssKid(kid);
		
		for (MacibuRezultati temp: macRez) {
			temp.setKurss(null);
		}
		
		ArrayList<Sertifikati> sertifikati = sertRepo.findByKurssKid(kid);
		for (Sertifikati temp: sertifikati) {
			temp.setKurss(null);
		}
		
		kurssRepo.deleteById(kid);
	}

}
