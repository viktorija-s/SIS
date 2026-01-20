package lv.sis.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lv.sis.model.KursaDatumi;
import lv.sis.model.Kurss;
import lv.sis.model.Pasniedzeji;
import lv.sis.repo.IKurssRepo;
import lv.sis.repo.IPasniedzejiRepo;
import lv.sis.service.ICRUDKursaDatumiService;

@Controller
@RequestMapping("kursaDatumi/CRUD")
public class KursaDatumiCRUDController {
	
	private static final Logger logger = LoggerFactory.getLogger(KursaDatumiCRUDController.class);

	@Autowired
	private ICRUDKursaDatumiService kursaDatumiService;
	
	@Autowired
	private IPasniedzejiRepo pasniedzejiRepo;
	
	@Autowired
	private IKurssRepo kurssRepo;
	
	@GetMapping("/show/all")
	public String getControllerShowAllKursaDatumi(Model model,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "3") int size) {
		logger.info("Request received to retrieve all course dates with pagination (page={}, size={})", page, size);
		
		try {
			Pageable pageable = PageRequest.of(page, size);
			Page<KursaDatumi> visiKursaDatumi = kursaDatumiService.retrieveAll(pageable); 
			model.addAttribute("package", visiKursaDatumi);
			logger.info("Successfully retrieved course dates list");
			return "kursa-datumi-all-page";
		} catch (Exception e) {
			logger.error("Failed to retrieve list of course dates", e);
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}
	
	@GetMapping("/show/all/{id}")
	public String getControllerShowKursaDatumsByID(@PathVariable(name = "id") Integer id, Model model) {
		logger.info("Request received to retrieve course date by ID={}", id);
		
		try {
			KursaDatumi kursaDatumi = kursaDatumiService.retrieveById(id);
			model.addAttribute("package", kursaDatumi);
			logger.info("Successfully retrieved course date with ID={}", id);
			return "kursa-datumi-all-page";
		} catch (Exception e) {
			logger.error("Failed to retrieve course date with ID={}", id, e);
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}
	
	@GetMapping("/remove/{id}")
	public String getControllerRemoveKursaDatumi(@PathVariable(name = "id") int id, Model model) {
		logger.info("Request received to prepare deletion of course date with ID={}", id);
		
        try {
			KursaDatumi kursaDatumi = kursaDatumiService.retrieveById(id);
			model.addAttribute("kursaDatumi", kursaDatumi);
			logger.info("Deletion confirmation page prepared for course date ID={}", id);
			return "kursa-datumi-delete-confirm";
		} catch (Exception e) {
			logger.error("Failed to prepare deletion for course date with ID={}", id, e);
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}

	@PostMapping("/remove/{id}")
	public String deleteConfirmed(@PathVariable(name = "id") int id) {
		logger.warn("Deletion request confirmed for course date with ID={}", id);
		
		try {
			kursaDatumiService.deleteById(id);
			logger.info("Course date successfully deleted (ID={})", id);
			return "redirect:/kursaDatumi/CRUD/show/all";
		} catch (Exception e) {
			logger.error("Failed to delete course date with ID={}", id, e);
			return "error-page";
		}
	}
	
	@GetMapping("/add")
    public String getControllerAddKursaDatumi(Model model) {
		logger.info("Request received to open course date creation form");
		
        KursaDatumi kursaDatumi = new KursaDatumi();
        model.addAttribute("kursaDatumi", kursaDatumi);
        
        List<Kurss> kurssList = kurssRepo.findAll();
        model.addAttribute("kurssList", kurssList);

        List<Pasniedzeji> pasniedzejiList = new ArrayList<>();
        pasniedzejiRepo.findAll().forEach(pasniedzejiList::add);
        model.addAttribute("pasniedzejiList", pasniedzejiList);
        
        return "kursa-datumi-add-page";
    }
	
	@PostMapping("/add")
    public String postControllerAddKursaDatumi(@ModelAttribute KursaDatumi kursaDatumi, Model model) {
		logger.info("Request received to create new course date");
		
        if (kursaDatumi == null) {
            model.addAttribute("package", "The kursa datumi is not given");
        }
        try {
            kursaDatumiService.create(
                kursaDatumi.getSakumaDatums(),
                kursaDatumi.getBeiguDatums(),
                kursaDatumi.getKurss(),
                kursaDatumi.getPasniedzejs()
            );
            logger.info("New course date successfully created");
            return "redirect:/kursaDatumi/CRUD/show/all";
        } catch (Exception e) {
        	logger.error("Failed to create new course date", e);
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }
	
	@GetMapping("/update/{id}")
    public String getControllerUpdateKursaDatumi(@PathVariable(name = "id") int id, Model model) {
		logger.info("Request received to open update form for course date with ID={}", id);
		
        try {
            KursaDatumi kursaDatumi = kursaDatumiService.retrieveById(id);
            model.addAttribute("kursaDatumi", kursaDatumi);
            
            List<Pasniedzeji> pasniedzejiList = new ArrayList<>();
            pasniedzejiRepo.findAll().forEach(pasniedzejiList::add);
            model.addAttribute("pasniedzejiList", pasniedzejiList);
            
            logger.info("Update form prepared for course date ID={}", id);
            
            return "kursa-datumi-update-page";
        } catch (Exception e) {
        	logger.error("Failed to prepare update form for course date ID={}", id, e);
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }
	
	 @PostMapping("/update/{id}")
	 public String postControllerUpdateKursaDatumi(@PathVariable(name = "id") int id, KursaDatumi kursaDatumi, Model model) {
		 logger.info("Request received to update course date with ID={}", id);
		 
	     try {
	         kursaDatumiService.updateById(id, kursaDatumi);
	         logger.info("Course date successfully updated (ID={})", id);
	         return "redirect:/kursaDatumi/CRUD/show/all";
	     } catch (Exception e) {
	    	 logger.error("Failed to update course date with ID={}", id, e);
	         model.addAttribute("package", e.getMessage());
	         return "error-page";
	     }
	}
	
}
