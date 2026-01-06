package lv.sis.service.impl;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lv.sis.model.KursaDatumi;
import lv.sis.model.Kurss;
import lv.sis.model.Pasniedzeji;
import lv.sis.repo.IKursaDatumiRepo;
import lv.sis.repo.IPasniedzejiRepo;
import lv.sis.service.ICRUDKursaDatumiService;

@Service
public class CRUDKursaDatumiServiceImpl implements ICRUDKursaDatumiService {
	
	@Autowired
	private IKursaDatumiRepo kursaDatumiRepo;
	
	@Autowired
	private IPasniedzejiRepo pasnRepo;

	@Override
	public void create(LocalDate sakumaDatums, LocalDate beiguDatums, Kurss kurss, Pasniedzeji pasniedzejs) throws Exception {	
		if (sakumaDatums == null || beiguDatums == null || kurss == null || pasniedzejs == null) {
            throw new Exception("Ievades parametri nav pareizi");
        }
		
		if (!sakumaDatums.isBefore(beiguDatums)) {
	        throw new Exception("Sākuma datumam jābūt pirms beigu datuma!");
	    }
		
		LocalDate today = LocalDate.now();
	    if (sakumaDatums.isBefore(today)) {
	        throw new Exception("Sākuma datums nevar būt pagātnē!");
	    }
	    if (beiguDatums.isBefore(today)) {
	        throw new Exception("Beigu datums nevar būt pagātnē!");
	    }
	    
	    ArrayList<KursaDatumi> visiKursaDatumi = kursaDatumiRepo.findAllByKurssKid(kurss.getKid());
	    
	    for (KursaDatumi existing : visiKursaDatumi) {
	        if (existing.getPasniedzejs().getPid() == pasniedzejs.getPid()) {
	            boolean overlaps = !(beiguDatums.isBefore(existing.getSakumaDatums()) || 
	                               sakumaDatums.isAfter(existing.getBeiguDatums()));
	            
	            if (overlaps) {
	                throw new Exception("Kursa datumi pārklājas ar esošu kursu '" + 
	                                  existing.getKurss().getNosaukums() + 
	                                  "' (ID: " + existing.getKursaDatId() + 
	                                  ") tam pašam pasniedzējam!");
	            }
	        }
	    }
	    
	    KursaDatumi kursaDatumi = new KursaDatumi(sakumaDatums, beiguDatums, kurss, pasniedzejs);
	    kursaDatumiRepo.save(kursaDatumi);

//        if (beiguDatums.isBefore(sakumaDatums)) {
//            throw new Exception("Beigu datums nevar būt pirms sākuma datuma!");
//        } else { 
//	        KursaDatumi kursaDatumi = new KursaDatumi(sakumaDatums, beiguDatums, kurss, pasniedzejs);
//	        kursaDatumiRepo.save(kursaDatumi);
//		
//        }
	}

	@Override
	public Page<KursaDatumi> retrieveAll(Pageable pageable) throws Exception {
		if (kursaDatumiRepo.count() == 0) {
            throw new Exception("Tabulā nav neviena kursa datumu ieraksta");
        }
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		
		for (GrantedAuthority a: auth.getAuthorities()) {
			if (a.getAuthority().equals("ADMIN")) {
				return kursaDatumiRepo.findAll(pageable); 
			}
		}
		
		Pasniedzeji professor = pasnRepo.findByUserUsername(username);
		if (professor == null) {
		    throw new Exception("Šim lietotājam nav piesaistīts pasniedzējs");
		}
		
		return kursaDatumiRepo.findAllByPasniedzejsPid(professor.getPid(), pageable);
	}

	@Override
	public KursaDatumi retrieveById(int kursaDatId) throws Exception {
		if (kursaDatId < 0) {
            throw new Exception("ID nevar būt negatīvs!");
        }

        if (!kursaDatumiRepo.existsById(kursaDatId)) {
            throw new Exception("Kursa datumi ar ID " + kursaDatId + " neeksistē!");
        }
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		
		for (GrantedAuthority a: auth.getAuthorities()) {
			if (a.getAuthority().equals("ADMIN")) {
				return kursaDatumiRepo.findById(kursaDatId).get(); 
			}
		}
		
		Pasniedzeji professor = pasnRepo.findByUserUsername(username);
		if (professor == null) {
		    throw new Exception("Šim lietotājam nav piesaistīts pasniedzējs");
		}
        KursaDatumi kursaDatumi = kursaDatumiRepo.findById(kursaDatId).get();
        if (professor.getPid() == kursaDatumi.getPasniedzejs().getPid()) {
        	return kursaDatumi;
        }
		throw new Exception("This user does not have rights to watch this page."); 
	}

	@Override
	public void updateById(int kursaDatId, KursaDatumi kursaDatumi) throws Exception {
		if (kursaDatId < 0) {
            throw new Exception("ID nevar būt negatīvs!");
        }

        if (!kursaDatumiRepo.existsById(kursaDatId)) {
            throw new Exception("Kursa datumi ar ID " + kursaDatId + " neeksistē!");
        }

        KursaDatumi retrievedKursaDatumi = kursaDatumiRepo.findById(kursaDatId).get();
        
        LocalDate newSakumaDatums = retrievedKursaDatumi.getSakumaDatums();
        LocalDate newBeiguDatums = retrievedKursaDatumi.getBeiguDatums();
        Kurss newKurss = retrievedKursaDatumi.getKurss();
        Pasniedzeji newPasniedzejs = retrievedKursaDatumi.getPasniedzejs();

        if (kursaDatumi.getSakumaDatums() != null && !kursaDatumi.getSakumaDatums().equals(retrievedKursaDatumi.getSakumaDatums())) {
        	retrievedKursaDatumi.setSakumaDatums(kursaDatumi.getSakumaDatums());
        }

        if (kursaDatumi.getBeiguDatums() != null && !kursaDatumi.getBeiguDatums().equals(retrievedKursaDatumi.getBeiguDatums())) {
        	retrievedKursaDatumi.setBeiguDatums(kursaDatumi.getBeiguDatums());
        }

        if (kursaDatumi.getKurss() != null && !kursaDatumi.getKurss().equals(retrievedKursaDatumi.getKurss())) {
        	retrievedKursaDatumi.setKurss(kursaDatumi.getKurss());
        }

        if (kursaDatumi.getPasniedzejs() != null && !kursaDatumi.getPasniedzejs().equals(retrievedKursaDatumi.getPasniedzejs())) {
        	retrievedKursaDatumi.setPasniedzejs(kursaDatumi.getPasniedzejs());
        }
        
        if (!newSakumaDatums.isBefore(newBeiguDatums)) {
            throw new Exception("Sākuma datumam jābūt pirms beigu datuma!");
        }
        
        LocalDate today = LocalDate.now();
        if (newSakumaDatums.isBefore(today)) {
            throw new Exception("Sākuma datums nevar būt pagātnē!");
        }
        if (newBeiguDatums.isBefore(today)) {
            throw new Exception("Beigu datums nevar būt pagātnē!");
        }
        
        ArrayList<KursaDatumi> visiKursaDatumi = kursaDatumiRepo.findAllByKurssKid(newKurss.getKid());
        
        for (KursaDatumi existing : visiKursaDatumi) {
            if (existing.getKursaDatId() == kursaDatId) {
                continue;
            }
          
            if (existing.getPasniedzejs().getPid() == newPasniedzejs.getPid()) {
                boolean overlaps = !(newBeiguDatums.isBefore(existing.getSakumaDatums()) || 
                                   newSakumaDatums.isAfter(existing.getBeiguDatums()));
                
                if (overlaps) {
                    throw new Exception("Kursa datumi pārklājas ar esošu kursu '" + 
                                      existing.getKurss().getNosaukums() + 
                                      "' (ID: " + existing.getKursaDatId() + 
                                      ") tam pašam pasniedzējam!");
                }
            }
        }
        
        retrievedKursaDatumi.setSakumaDatums(newSakumaDatums);
        retrievedKursaDatumi.setBeiguDatums(newBeiguDatums);
        retrievedKursaDatumi.setKurss(newKurss);
        retrievedKursaDatumi.setPasniedzejs(newPasniedzejs);

        kursaDatumiRepo.save(retrievedKursaDatumi);
		
	}

	@Override
	public void deleteById(int kursaDatId) throws Exception {
		if (kursaDatId < 0) {
            throw new Exception("ID nevar būt negatīvs!");
        }

        if (!kursaDatumiRepo.existsById(kursaDatId)) {
            throw new Exception("Kursa datumi ar ID " + kursaDatId + " neeksistē!");
        }

        kursaDatumiRepo.deleteById(kursaDatId);
    
	}
	
}
