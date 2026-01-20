package lv.sis.controller;


import lv.sis.service.IFilterService;

import java.util.ArrayList;

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

import lv.sis.model.Kurss;
import lv.sis.model.enums.Limeni;
import lv.sis.service.ICRUDKurssService;

@Controller
@RequestMapping("kurss/CRUD")
public class KurssCRUDController {
	
	private static final Logger logger = LoggerFactory.getLogger(KurssCRUDController.class);
	
	@Autowired 
	private ICRUDKurssService kurssService;
	
	@Autowired
	private IFilterService filterService;
	
	@GetMapping("/show/all") // localhost:8080/kurss/CRUD/show/all?page=0&size=3
	public String getControllerShowAllKursi(Model model, 
											@RequestParam(defaultValue = "0") int page, 
											@RequestParam(defaultValue = "3") int size,
											@RequestParam(required = false) String search
											) {
		logger.info("Request received to retrieve all courses (page={}, size={}, search={})", page, size, search);
		
		try {
			ArrayList<Kurss> kursi;
			if (search != null && !search.trim().isEmpty()) {
                kursi = filterService.findByNosaukumsContainingIgnoreCase(search.trim());
                model.addAttribute("search", search);
                model.addAttribute("package", kursi);
                logger.info("Filtered courses returned: {} results for search='{}'", kursi.size(), search);
			}
			else {
				Pageable pageable = PageRequest.of(page, size);
				Page<Kurss> visiKursi = kurssService.retrieveAll(pageable); 
				model.addAttribute("package", visiKursi);
				logger.info("Returned all courses: {} entries", visiKursi.getTotalElements());
			}
			return "kurss-all-page"; 
		} catch (Exception e) {
			logger.error("Failed to retrieve list of courses", e);
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}
	@GetMapping("/show/all/{id}")
	public String getControllerShowKurssByID(@PathVariable(name = "id") Integer id, Model model) {
		logger.info("Request received to retrieve course by ID={}", id);
		
		try {
			Kurss kurss = kurssService.retrieveById(id);
			model.addAttribute("package", kurss);
			logger.info("Successfully retrieved course with ID={}", id);
			return "kurss-all-page";
		} catch (Exception e) {
			logger.error("Failed to retrieve course with ID={}", id, e);
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}
	
	@GetMapping("/remove/{id}")
	public String getControllerRemoveKurss(@PathVariable(name = "id") int id, Model model) {
		logger.info("Request received to prepare deletion of course with ID={}", id);
		
		try {
			Kurss kurss = kurssService.retrieveById(id);
			model.addAttribute("kurss", kurss);
			logger.info("Deletion confirmation page prepared for course ID={}", id);
			return "kurss-delete-confirm";
		} catch (Exception e) {
			logger.error("Failed to prepare deletion for course with ID={}", id, e);
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}
	
	@PostMapping("/remove/{id}")
	public String deleteConfirmed(@PathVariable(name = "id") int id) {
		logger.warn("Deletion request confirmed for course with ID={}", id);
		
		try {
			kurssService.deleteById(id);
			logger.info("Course successfully deleted: ID={}", id);
			return "redirect:/kurss/CRUD/show/all";
		} catch (Exception e) {
			logger.error("Failed to delete course: ID={}", id, e);
			return "error-page";
		}
	}
	
	@GetMapping("/add")
	public String getControllerAddKurss(Model model) {
		logger.info("Request received to open course creation form");
		
		Kurss kurss = new Kurss();
		model.addAttribute("limeni", Limeni.values());
		model.addAttribute("kurss", kurss);
		return "kurss-add-page";
	}
	@PostMapping("/add")
	public String postControllerAddKurss(@ModelAttribute Kurss kurss, Model model) {
		logger.info("Request received to create new course");
		
        if (kurss == null || kurss.getStundas() < 1) {
        	logger.warn("Invalid input parameters for course creation: name={}, stundas={}", kurss.getNosaukums(), kurss.getStundas());
            model.addAttribute("message", "Nav ievadīts kursa nosaukums vai stundas!");
            model.addAttribute("limeni", Limeni.values());
            return "kurss-add-page";
        }

        try {
			kurssService.create(kurss.getNosaukums(), kurss.getStundas(), kurss.getLimenis());
            model.addAttribute("kurss", new Kurss());
            model.addAttribute("limeni", Limeni.values());
            logger.info("New course successfully created");
			return "redirect:/kurss/CRUD/show/all";
		} catch (Exception e) {
			logger.error("Failed to create new course", e);
            model.addAttribute("message", e.getMessage());
            model.addAttribute("limeni", Limeni.values());
            return "kurss-add-page";
		}
	}

	@GetMapping("/update/{id}")
	public String getControllerUpdateKurss(@PathVariable(name = "id") int id, Model model) {
		logger.info("Request received to open update form for course with ID={}", id);
		
		try {
			Kurss kurss = kurssService.retrieveById(id);
			model.addAttribute("kurss", kurss);
			model.addAttribute("limeni", Limeni.values());
			return "kurss-update-page";
		} catch (Exception e) {
			logger.error("Failed to prepare update form for course ID={}", id, e);
            model.addAttribute("package", e.getMessage());
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}
	
	@PostMapping("/update/{id}")
	public String postControllerUpdateKurss(@PathVariable(name = "id") int id, Kurss kurss, Model model) {
		logger.info("Request received to update course with ID={}", id);
		
		try {
			kurssService.updateById(id, kurss.getNosaukums(), kurss.getStundas(), kurss.getLimenis());
			logger.info("Course successfully updated (ID={})", id);
			return "redirect:/kurss/CRUD/show/all";
		} catch (Exception e) {
			logger.error("Failed to update course with ID={}", id, e);
			model.addAttribute("package", e.getMessage()); 
			return "error-page"; 
		}
	}

//    @GetMapping("/filter/kurss")
//    public String getControllerFilterKurssByName(String text, Model model) {
//        try {
//            ArrayList<Kurss> visiKursi = filterService.findByNameContainingText(text);
//            model.addAttribute("package", visiKursi);
//            return kurss
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        model.addAttribute("package", kursiPecNosaukuma);
//    }


}
