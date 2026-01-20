package lv.sis.controller;

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
import org.springframework.web.multipart.MultipartFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lv.sis.model.KursaDalibnieki;
import lv.sis.service.ICRUDKursaDalibniekiService;

@Controller
@RequestMapping("kursaDalibnieki/CRUD")
public class KursaDalibniekiCRUDController {
	
	private static final Logger logger = LoggerFactory.getLogger(KursaDalibniekiCRUDController.class);
	
	@Autowired
	private ICRUDKursaDalibniekiService kursaDalibniekiServiss;
 
	@GetMapping("/show/all")
	public String getControllerShowAllKursaDalibnieki(Model model, 
			@RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "3") int size) {
		logger.info("Request received to retrieve all course participants with pagination (page={}, size={})", page, size);
		
		try {
			Pageable pageable = PageRequest.of(page, size);
			Page<KursaDalibnieki> visiKursaDalibnieki = kursaDalibniekiServiss.retrieveAll(pageable);
			model.addAttribute("kursaDal", visiKursaDalibnieki);
			logger.info("Successfully retrieved course participant list");
			return "kursa-dalibnieki-all-page";
		} catch (Exception e) {
			logger.error("Failed to retrieve list of course participants", e);
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}

	@GetMapping("/show/all/{id}")
	public String getControllerShowKursaDalibnieksByID(@PathVariable(name = "id") Integer id, Model model) {
		logger.info("Request received to retrieve course participant by ID={}", id);
		
		try {
			KursaDalibnieki kursaDalibnieki = kursaDalibniekiServiss.retrieveById(id);
			model.addAttribute("kursaDal", kursaDalibnieki);
			logger.info("Successfully retrieved course participant with ID={}", id);
			return "kursa-dalibnieki-all-page";
		} catch (Exception e) {
			logger.error("Failed to retrieve course participant with ID={}", id, e);
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}

	@GetMapping("/remove/{id}")
	public String getControllerRemoveKursaDalibnieku(@PathVariable(name = "id") int id, Model model) {
		logger.info("Request received to prepare deletion of course date with ID={}", id);
		
		try {
			KursaDalibnieki dalibnieks = kursaDalibniekiServiss.retrieveById(id);
	        model.addAttribute("kursaDalibnieks", dalibnieks);
	        logger.info("Deletion confirmation page prepared for course participant ID={}", id);
	        return "kursa-dalibnieks-delete-confirm";
		} catch (Exception e) {
			logger.error("Failed to prepare deletion for course participant with ID={}", id, e);
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}
	
	@PostMapping("/remove/{id}")
	public String deleteConfirmed(@PathVariable(name = "id") int id) {
		logger.warn("Deletion request confirmed for course date with ID={}", id);
		
	    try {
	        kursaDalibniekiServiss.deleteById(id);
	        logger.info("Course participant successfully deleted (ID={})", id);
	        return "redirect:/kursaDalibnieki/CRUD/show/all";
	    } catch (Exception e) {
	    	logger.error("Failed to delete course participant with ID={}", id, e);
//	    	e.printStackTrace();
	        return "error-page";
	    }
	}

	@GetMapping("/add")
	public String getControllerAddKursaDalibnieku(Model model) {
		logger.info("Request received to open course participant creation form");
		
		KursaDalibnieki kursaDalibnieki = new KursaDalibnieki();
		model.addAttribute("KursaDalibnieki", kursaDalibnieki);
		return "kursa-dalibnieki-add-page";
	}

	@PostMapping("/add")
	public String postControllerAddKursaDalibnieku(@ModelAttribute KursaDalibnieki kursaDalibnieki, Model model) {
		logger.info("Request received to create new course participant");
		
		if (kursaDalibnieki == null) {
			logger.warn("Attempted to add null course participant");
			model.addAttribute("package", "The kursa dalibnieks is not given");
		}

		try {
			System.out.println(kursaDalibnieki);
			kursaDalibniekiServiss.create(kursaDalibnieki.getVards(), kursaDalibnieki.getUzvards(),
					kursaDalibnieki.getEpasts(), kursaDalibnieki.getTelefonaNr(), kursaDalibnieki.getPersonasId(),
					kursaDalibnieki.getPilseta(), kursaDalibnieki.getValsts(),
					kursaDalibnieki.getIelasNosaukumsNumurs(), kursaDalibnieki.getDzivoklaNr(),
					kursaDalibnieki.getPastaIndekss());
			logger.info("New course participant successfully created: {} {}", kursaDalibnieki.getVards(), kursaDalibnieki.getUzvards());
			return "redirect:/kursaDalibnieki/CRUD/show/all";
		} catch (Exception e) {
			logger.error("Failed to create new course participant", e);
			model.addAttribute("package", e.getMessage());
//			e.printStackTrace();
			return "error-page";
		}
	}
	
	@GetMapping("/update/{id}")
	public String getControllerUpdateKursaDalibnieks(@PathVariable(name = "id") int id, Model model) {
		logger.info("Request received to open update form for course participant with ID={}", id);
		
		try {
			KursaDalibnieki kursaDalibnieki = kursaDalibniekiServiss.retrieveById(id);
			model.addAttribute("kursaDalibnieki", kursaDalibnieki);
			logger.info("Update form prepared for course participant ID={}", id);
			return "kursa-dalibnieki-update-page";
		} catch (Exception e) {
			logger.error("Failed to prepare update form for course participant ID={}", id, e);
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}
	
	@PostMapping("/update/{id}")
	public String postControllerUpdateDoctor(@PathVariable(name = "id") int id, KursaDalibnieki kursaDalibnieki, Model model) {
		logger.info("Request received to update course participant with ID={}", id);
		
		try {
			kursaDalibniekiServiss.updateById(id, kursaDalibnieki.getVards(), kursaDalibnieki.getUzvards(), kursaDalibnieki.getEpasts(), kursaDalibnieki.getTelefonaNr(), kursaDalibnieki.getPersonasId(), kursaDalibnieki.getPilseta(), kursaDalibnieki.getValsts(), kursaDalibnieki.getIelasNosaukumsNumurs(), kursaDalibnieki.getDzivoklaNr(), kursaDalibnieki.getPastaIndekss());
			logger.info("Course participant successfully updated (ID={})", id);
			return "redirect:/kursaDalibnieki/CRUD/show/all/" + id;
		} catch (Exception e) {
			logger.error("Failed to update course participant with ID={}", id, e);
			model.addAttribute("package", e.getMessage()); 
//			e.printStackTrace();
			return "error-page"; 
		}
	}
	
	@GetMapping("/import") //localhost:8080/kursaDalibnieki/CRUD/import
	public String getControllerImportCourseParticipants(Model model) {
		logger.info("Request received to import course participant");
	    return "kursa-dalibnieki-import-page";
	}

	@PostMapping("/import")
	public String postControllerImportCourseParticipants(@RequestParam("file") MultipartFile file, Model model) {
		logger.info("Request received to import course participants from uploaded file: {}", file.getOriginalFilename());
		
	    try {
	        kursaDalibniekiServiss.importCourseParticipants(file);
	        logger.info("Imported course participants from file={}", file.getOriginalFilename());
	        return "redirect:/kursaDalibnieki/CRUD/show/all";
	    } catch (Exception e) {
	    	logger.error("Error importing course participants from file={}", file.getOriginalFilename(), e);
	        model.addAttribute("package", e.getMessage());
//	        e.printStackTrace();
	        return "error-page";
	    }
	}

}
