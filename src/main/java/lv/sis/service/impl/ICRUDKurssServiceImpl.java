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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lv.sis.service.ICRUDKurssService;
import lv.sis.service.IUserService;

@Service
public class ICRUDKurssServiceImpl implements ICRUDKurssService{
	
	private static final Logger logger = LoggerFactory.getLogger(ICRUDKurssServiceImpl.class);
	
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
		  logger.info("Attempting to create new course: name={}, stundas={}, limenis={}", nosaukums, stundas, limenis);
		
		if (nosaukums == null || stundas<0 || limenis == null) {
			logger.warn("Course creation failed due to missing input parameters");
			throw new Exception("Dati nav pareizi");
		}
		if (kurssRepo.existsByNosaukums(nosaukums)) {
			logger.warn("Course already exists with the name={}", nosaukums);
			throw new Exception("Tads kurss jau eksistē");
		}
		
		Kurss newKurss = new Kurss(nosaukums, stundas, limenis);
		kurssRepo.save(newKurss);
		logger.info("Course successfully created: {}", nosaukums);
	}

	@Override
	public Page<Kurss> retrieveAll(Pageable pageable) throws Exception {
		logger.info("Request received to retrieve course list");
		
		if (kurssRepo.count() == 0) {
			logger.warn("No courses found in database");
			throw new Exception("Tabula ir tukša");
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		
		for (GrantedAuthority a: auth.getAuthorities()) {
			if (a.getAuthority().equals("ADMIN")) {
				logger.info("Admin access detected - returning all courses");
				return kurssRepo.findAll(pageable); 
			}
		}
		
		Pasniedzeji professor = pasnRepo.findByUserUsername(username);
		return kurssRepo.findAllByKursaDatumiPasniedzejsPid(professor.getPid(), pageable);
		
	
	}

	@Override
	public Kurss retrieveById(int kdid) throws Exception {
		logger.info("Request received to retrieve course details: ID={}", kdid);
		
		if (kdid < 0) {
			logger.warn("Negative course ID provided: {}", kdid);
			throw new Exception("ID nav pareizs");
		}
		if (!kurssRepo.existsById(kdid)) {
			logger.warn("Course not found: ID={}", kdid);
			throw new Exception("Kurss ar tadu id neeksistē");
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		
		for (GrantedAuthority a: auth.getAuthorities()) {
			if (a.getAuthority().equals("ADMIN")) {
				logger.info("Admin access granted to course: ID={}", kdid);
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
		logger.info("Request received to update course: ID={}", kdid);
		
		if (kdid < 0) {
			logger.warn("Negative course ID provided for update: {}", kdid);
			throw new Exception("ID nav pareizs");
		}
		if (!kurssRepo.existsById(kdid)) {
			logger.warn("Attempt to update non-existing course: ID={}", kdid);
			throw new Exception("Kurss ar tadu id neeksistē");
		}
		
		Kurss selectedKurss = kurssRepo.findById(kdid).get();
		
		selectedKurss.setNosaukums(nosaukums);
		selectedKurss.setStundas(stundas);
		selectedKurss.setLimenis(limenis);
		
		kurssRepo.save(selectedKurss);
		logger.info("Course successfully updated ID={}", kdid);
	}

	@Override
	public void deleteById(int kid) throws Exception {
		logger.info("Request received to delete course schedule: ID={}", kid);
		
		if(kid < 0) {
			logger.warn("Negative course ID provided for deletion: {}", kid);
			throw new Exception("Id nevar būt negatīvs");
		}
		
		if(!kurssRepo.existsById(kid)) {
			logger.warn("Attempt to delete non-existing course: ID={}", kid);
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
		logger.info("Course successfully deleted: ID={}", kid);
	}

}
