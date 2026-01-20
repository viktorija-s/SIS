package lv.sis.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import lv.sis.repo.*;
import lv.sis.service.IUserService;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import lv.sis.repo.IMacibuRezultatiRepo;
import lv.sis.repo.IPasniedzejiRepo;

import lv.sis.service.ICRUDKurssService;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ICRUDKurssServiceImpl implements ICRUDKurssService{
	
	private final IKurssRepo kurssRepo;
    private final ISertifikatiRepo sertRepo;
    private final IMacibuRezultatiRepo macRezRepo;
    private final IPasniedzejiRepo pasnRepo;
    private final IUserService userService;
    
    public ICRUDKurssServiceImpl(IKurssRepo kurssRepo, ISertifikatiRepo sertRepo, IMacibuRezultatiRepo macRezRepo,
            IPasniedzejiRepo pasnRepo, IUserService userService) {
        this.kurssRepo = kurssRepo;
        this.sertRepo = sertRepo;
        this.macRezRepo = macRezRepo;
        this.pasnRepo = pasnRepo;
        this.userService = userService;
    }

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

    @Transactional
    @Override
    public void importCourses(MultipartFile file) throws Exception {

        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {

            XSSFSheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);
                if (row == null) continue;

                String nosaukums = formatter.formatCellValue(row.getCell(0));
                int stundas = (int) row.getCell(1).getNumericCellValue();
                Limeni limenis = Limeni.valueOf(
                        formatter.formatCellValue(row.getCell(2))
                );

                create(nosaukums, stundas, limenis);
            }
        }
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
	public Page<Kurss> retrieveById(int kdid) throws Exception {
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
				
				Optional<Kurss> kurssOpt = kurssRepo.findById(kdid);
	            if (!kurssOpt.isPresent()) {
	                throw new Exception("Kurss neeksistē");
	            }
	            Kurss kurss = kurssOpt.get();
				
				Pageable pageable = PageRequest.of(0, 1);
				return new PageImpl<>(List.of(kurss), pageable, 1);
			}
		}

		Pasniedzeji pasn = pasnRepo.findByUserUsername(username);
		
		Optional<Kurss> courseOpt = kurssRepo.findById(kdid);
	    if (!courseOpt.isPresent()) {
	        throw new Exception("Kurss neeksistē");
	    }
	    Kurss course = courseOpt.get();
		
		for (KursaDatumi kd : course.getKursaDatumi()) {
			if (pasn.getPid() == kd.getPasniedzejs().getPid()) {
				Pageable pageable = PageRequest.of(0, 1);
				return new PageImpl<>(List.of(course), pageable, 1);
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
		
		if (nosaukums == null || nosaukums.isBlank() || stundas<0 || limenis == null) {
			throw new Exception("Dati nav pareizi");
		}
		
		Optional<Kurss> kurssOpt = kurssRepo.findById(kdid);
	    if (!kurssOpt.isPresent()) {
	        throw new Exception("Kurss neeksistē");
	    }
	    Kurss selectedKurss = kurssOpt.get();
		
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
