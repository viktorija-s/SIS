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

import lv.sis.model.Kurss;
import lv.sis.model.enums.Limeni;
import lv.sis.service.ICRUDKurssService;

@Controller
@RequestMapping("kurss/CRUD")
public class KurssCRUDController {
	@Autowired 
	private ICRUDKurssService kurssserviss;
	
	@GetMapping("/show/all")
	public String getControllerShowAllKursi(Model model) {
		try {
			ArrayList<Kurss> visiKursi = kurssserviss.retrieveAll(); 
			model.addAttribute("package", visiKursi);
			return "kurss-all-page"; 
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}
	@GetMapping("/show/all/{id}")
	public String getControllerShowKurssByID(@PathVariable(name = "id") Integer id, Model model) {
		try {
			Kurss kurss = kurssserviss.retrieveById(id);
			model.addAttribute("package", kurss);
			return "kurss-all-page";
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}
	
	@GetMapping("/remove/{id}")
	public String getControllerRemoveKurss(@PathVariable(name = "id") int id, Model model) {
		try {
			kurssserviss.deleteById(id);
			model.addAttribute("package", kurssserviss.retrieveAll());
			return "kurss-all-page";
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}
	
	@GetMapping("/add")
	public String getControllerAddKurss(Model model) {
		Kurss kurss = new Kurss();
		model.addAttribute("limeni", Limeni.values());
		model.addAttribute("kurss", kurss);
		return "kurss-add-page";
	}
	@PostMapping("/add")
	public String postControllerAddKurss(@ModelAttribute Kurss kurss, Model model) {
		if (kurss == null) {
			model.addAttribute("package", "The kurss is not given");
		}
		
		try {
			System.out.println(kurss);
			kurssserviss.create(kurss.getNosaukums(), kurss.getStundas(), kurss.getLimenis());
			return "redirect:/kurss/CRUD/show/all";
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			e.printStackTrace();
			return "error-page";
		}
	}
	@GetMapping("/update/{id}")
	public String getControllerUpdateKurss(@PathVariable(name = "id") int id, Model model) {
		try {
			Kurss kurss = kurssserviss.retrieveById(id);
			model.addAttribute("kurss", kurss);
			return "kurss-update-page";
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}
	
	@PostMapping("/update/{id}")
	public String postControllerUpdateKurss(@PathVariable(name = "id") int id, Kurss kurss, Model model) {
		try {
			kurssserviss.updateById(id, kurss.getNosaukums(), kurss.getStundas(), kurss.getLimenis());
			return "redirect:/kurss/CRUD/show/all";
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage()); 
			e.printStackTrace();
			return "error-page"; 
		}
	}
}
