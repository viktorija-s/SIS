package lv.sis.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lv.sis.model.KursaDalibnieki;
import lv.sis.model.KursaDatumi;
import lv.sis.model.Pasniedzeji;
import lv.sis.model.Vertejumi;
import lv.sis.repo.IPasniedzejiRepo;
import lv.sis.repo.IVertejumiRepo;
import lv.sis.service.ICRUDVertejumiService;

@Service
public class CRUDVertejumiServiceImpl implements ICRUDVertejumiService {

	@Autowired
	private IVertejumiRepo vertejumiRepo;
	
	@Autowired
	private IPasniedzejiRepo pasnRepo;

	@Override
	public void create(float vertejumi, LocalDate datums, KursaDalibnieki kursaDalibnieki, KursaDatumi kursaDatumi) throws Exception {
		if (vertejumi < 0 || datums == null || kursaDalibnieki == null || kursaDatumi == null) {
			throw new Exception("Ievades parametri nav pareizi");
		}

        if (!vertejumiRepo.existsByVertejumsAndDatums(vertejumi, datums) && !datums.isAfter(LocalDate.now())
                && !datums.isBefore(LocalDate.now().minusMonths(3))) {
            Vertejumi newVertejumi = new Vertejumi(vertejumi, datums, kursaDalibnieki, kursaDatumi);
            vertejumiRepo.save(newVertejumi);
        } else {
			throw new Exception("Šāds vērtējums jau eksistē vai ir norādīts nepareizs datums.");
		}
	} 
	
	@Override
	public Page<Vertejumi> retrieveAll(Pageable pageable) throws Exception {
		if(vertejumiRepo.count()==0) {
			throw new Exception("Tabulā nav neviena ieraksta");
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		
		for (GrantedAuthority a: auth.getAuthorities()) {
			if (a.getAuthority().equals("ADMIN")) {
				return vertejumiRepo.findAll(pageable); 
			}
		}
		
		Pasniedzeji professor = pasnRepo.findByUserUsername(username);
		if (professor == null) {
		    throw new Exception("Šim lietotājam nav piesaistīts pasniedzējs");
		}
		return vertejumiRepo.findAllByKursaDatumiPasniedzejsPid(professor.getPid(), pageable);
	}

	@Override
	public Page<Vertejumi> retrieveById(int vid) throws Exception {
		if (vid < 0) {
			throw new Exception("Id nevar būt negatīvs");
		}

		if (!vertejumiRepo.existsById(vid)) {
			throw new Exception("Vērtējumi ar tādu id neeksistē");
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		
		for (GrantedAuthority a: auth.getAuthorities()) {
			if (a.getAuthority().equals("ADMIN")) {
				Vertejumi vert = vertejumiRepo.findById(vid).get(); 
				Pageable pageable = PageRequest.of(0, 1);
				return new PageImpl<>(List.of(vert), pageable, 1);
			}
		}
		
		Pasniedzeji professor = pasnRepo.findByUserUsername(username);
		if (professor == null) {
		    throw new Exception("Šim lietotājam nav piesaistīts pasniedzējs");
		}
		Vertejumi vert = vertejumiRepo.findById(vid).get(); 
		if (professor.getPid() == vert.getKursaDatumi().getPasniedzejs().getPid()) {
			Pageable pageable = PageRequest.of(0, 1);
			return new PageImpl<>(List.of(vert), pageable, 1);
		}
		
		throw new Exception("This user does not have rights to watch this page."); 
	}

	@Override
	public void updateById(int vid, float vertejums) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		Vertejumi retrievedVertejumi = vertejumiRepo.findById(vid).get();
		
		for (GrantedAuthority a: auth.getAuthorities()) {
			if (a.getAuthority().equals("ADMIN")) {
				retrievedVertejumi.setVertejums(vertejums);
			}
		}
		
		Pasniedzeji prof = pasnRepo.findByUserUsername(username);
		if (prof == null) {
		    throw new Exception("Šim lietotājam nav piesaistīts pasniedzējs");
		}
		if (prof.getPid() == retrievedVertejumi.getKursaDatumi().getPasniedzejs().getPid()) {
			retrievedVertejumi.setVertejums(vertejums);
		} 
		else {
			throw new Exception("This user does not have rights to update this grade."); 
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
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		
		for (GrantedAuthority a: auth.getAuthorities()) {
			if (a.getAuthority().equals("ADMIN")) {
				vertejumiRepo.deleteById(vid); 
			}
		}
		
		Pasniedzeji prof = pasnRepo.findByUserUsername(username);
		if (prof == null) {
		    throw new Exception("Šim lietotājam nav piesaistīts pasniedzējs");
		}
		Vertejumi vert = vertejumiRepo.findById(vid).get();
		if (prof.getPid() == vert.getKursaDatumi().getPasniedzejs().getPid()) {
			vertejumiRepo.deleteById(vid); 
		}
		else {
			throw new Exception("This user does not have rights to delete this grade."); 
		}
	}

}
