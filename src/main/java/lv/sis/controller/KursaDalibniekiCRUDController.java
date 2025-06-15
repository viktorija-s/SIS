package lv.sis.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lv.sis.model.KursaDalibnieki;
import lv.sis.model.Kurss;
import lv.sis.model.enums.Limeni;
import lv.sis.service.ICRUDKursaDalibniekiService;

@Controller
@RequestMapping("kursaDalibnieki/CRUD")
public class KursaDalibniekiCRUDController {
	@Autowired
	private ICRUDKursaDalibniekiService kursaDalibniekiServiss;

	@GetMapping("/show/all")
	public String getControllerShowAllKursaDalibnieki(Model model) {
		try {
			ArrayList<KursaDalibnieki> visiKursaDalibnieki = kursaDalibniekiServiss.retrieveAll();
			model.addAttribute("package", visiKursaDalibnieki);
			return "kursa-dalibnieki-all-page";
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}

	@GetMapping("/show/all/{id}")
	public String getControllerShowKursaDalibnieksByID(@PathVariable(name = "id") Integer id, Model model) {
		try {
			KursaDalibnieki kursaDalibnieki = kursaDalibniekiServiss.retrieveById(id);
			model.addAttribute("package", kursaDalibnieki);
			return "kursa-dalibnieki-all-page";
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}

	@GetMapping("/remove/{id}")
	public String getControllerRemoveKursaDalibnieku(@PathVariable(name = "id") int id, Model model) {
		try {
			kursaDalibniekiServiss.deleteById(id);
			model.addAttribute("package", kursaDalibniekiServiss.retrieveAll());
			return "kursa-dalibnieki-all-page";
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}

	@GetMapping("/add")
	public String getControllerAddKursaDalibnieku(Model model) {
		KursaDalibnieki kursaDalibnieki = new KursaDalibnieki();
		model.addAttribute("KursaDalibnieki", kursaDalibnieki);
		return "kursa-dalibnieki-add-page";
	}

	@PostMapping("/add")
	public String postControllerAddKursaDalibnieku(@ModelAttribute KursaDalibnieki kursaDalibnieki, Model model) {
		if (kursaDalibnieki == null) {
			model.addAttribute("package", "The kursa dalibnieks is not given");
		}

		try {
			System.out.println(kursaDalibnieki);
			kursaDalibniekiServiss.create(kursaDalibnieki.getVards(), kursaDalibnieki.getUzvards(),
					kursaDalibnieki.getEpasts(), kursaDalibnieki.getTelefonaNr(), kursaDalibnieki.getPersonasId(),
					kursaDalibnieki.getPilseta(), kursaDalibnieki.getValsts(),
					kursaDalibnieki.getIelasNosaukumsNumurs(), kursaDalibnieki.getDzivoklaNr(),
					kursaDalibnieki.getPastaIndekss());
			return "redirect:/kursaDalibnieki/CRUD/show/all";
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			e.printStackTrace();
			return "error-page";
		}
	}
	
	@GetMapping("/update/{id}")
	public String getControllerUpdateKursaDalibnieks(@PathVariable(name = "id") int id, Model model) {
		try {
			KursaDalibnieki kursaDalibnieki = kursaDalibniekiServiss.retrieveById(id);
			model.addAttribute("kursaDalibnieki", kursaDalibnieki);
			return "kursa-dalibnieki-update-page";
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}
	
	@PostMapping("/update/{id}")
	public String postControllerUpdateDoctor(@PathVariable(name = "id") int id, KursaDalibnieki kursaDalibnieki, Model model) {
		try {
			kursaDalibniekiServiss.updateById(id, kursaDalibnieki.getVards(), kursaDalibnieki.getUzvards(), kursaDalibnieki.getEpasts(), kursaDalibnieki.getTelefonaNr(), kursaDalibnieki.getPersonasId(), kursaDalibnieki.getPilseta(), kursaDalibnieki.getValsts(), kursaDalibnieki.getIelasNosaukumsNumurs(), kursaDalibnieki.getDzivoklaNr(), kursaDalibnieki.getPastaIndekss());
			return "redirect:/kursaDalibnieki/CRUD/show/all";
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage()); 
			e.printStackTrace();
			return "error-page"; 
		}
	}

}
