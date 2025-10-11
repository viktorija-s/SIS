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

import lv.sis.model.Pasniedzeji;
import lv.sis.service.ICRUDPasniedzejiService;

@Controller
@RequestMapping("/pasniedzeji/CRUD")
public class PasniedzejiCRUDController {
	@Autowired 
	private ICRUDPasniedzejiService pasnService;
	
	@GetMapping("/show/all")
	public String getControllerShowAllPasniedzeji(Model model) {
		try {
			ArrayList<Pasniedzeji> visiKursi = pasnService.retrieveAll(); 
			model.addAttribute("package", visiKursi);
			return "pasniedzeji-all-page"; 
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}
	@GetMapping("/show/all/{id}")
	public String getControllerShowPasniedzejsByID(@PathVariable(name = "id") Integer id, Model model) {
		try {
			Pasniedzeji pasniedzejs = pasnService.retrieveById(id);
			model.addAttribute("package", pasniedzejs);
			return "pasniedzeji-all-page";
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}
	
	@GetMapping("/remove/{id}")
	public String getControllerRemovePasniedzejs(@PathVariable(name = "id") int id, Model model) {
		try {
			pasnService.deleteById(id);
			model.addAttribute("package", pasnService.retrieveAll());
			return "pasniedzeji-all-page";
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}
	
	@GetMapping("/add")
	public String getControllerAddPasniedzejs(Model model) {
		Pasniedzeji pasniedzejs = new Pasniedzeji();
		model.addAttribute("pasniedzejs", pasniedzejs);
		return "pasniedzeji-add-page";
	}
	@PostMapping("/add")
	public String postControllerAddPasniedzejs(@ModelAttribute Pasniedzeji pasniedzejs, Model model) {
		try {
			if (pasniedzejs == null) {
			model.addAttribute("package", "Pasniedzejs nav iedots");
		}
			pasnService.create(pasniedzejs.getVards(), pasniedzejs.getUzvards(), pasniedzejs.getEpasts(), pasniedzejs.getTelefonaNr());
			return "redirect:/pasniedzeji/CRUD/show/all";
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			e.printStackTrace();
			return "error-page";
		}
	}
	@GetMapping("/update/{id}")
	public String getControllerUpdatePasniedzejs(@PathVariable(name = "id") int id, Model model) {
		try {
			Pasniedzeji pasniedzejs = pasnService.retrieveById(id);
			model.addAttribute("pasniedzejs", pasniedzejs);
			return "pasniedzeji-update-page";
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}
	
	@PostMapping("/update/{id}")
	public String postControllerUpdatePasniedzejs(@PathVariable(name = "id") int id, Pasniedzeji pasniedzejs, Model model) {
		try {
			pasnService.updateById(id, pasniedzejs.getVards(), pasniedzejs.getUzvards(), pasniedzejs.getEpasts(), pasniedzejs.getTelefonaNr());
			return "redirect:/pasniedzeji/CRUD/show/all";
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage()); 
			e.printStackTrace();
			return "error-page"; 
		}
	}
}
