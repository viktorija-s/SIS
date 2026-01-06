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

import lv.sis.model.Pasniedzeji;
import lv.sis.service.ICRUDPasniedzejiService;

@Controller
@RequestMapping("/pasniedzeji/CRUD")
public class PasniedzejiCRUDController {
	@Autowired 
	private ICRUDPasniedzejiService pasnService;
	
	@GetMapping("/show/all")
	public String getControllerShowAllPasniedzeji(Model model, 
			@RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "3") int size) {
		try {
			Pageable pageable = PageRequest.of(page, size);
			Page<Pasniedzeji> visiKursi = pasnService.retrieveAll(pageable); 
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
			Page<Pasniedzeji> pasniedzejs = pasnService.retrieveById(id);
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
			Pasniedzeji pasniedzejs = pasnService.retrieveById(id).getContent().getFirst();
			model.addAttribute("pasniedzejs", pasniedzejs);
			return "pasniedzeji-delete-confirm";
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}

	@PostMapping("/remove/{id}")
	public String deleteConfirmed(@PathVariable(name = "id") int id) {
		try {
			pasnService.deleteById(id);
			return "redirect:/pasniedzeji/CRUD/show/all";
		} catch (Exception e) {
			e.printStackTrace();
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
            e.printStackTrace();
			model.addAttribute("message", e.getMessage());
			return "pasniedzeji-add-page";
		}
	}
	@GetMapping("/update/{id}")
	public String getControllerUpdatePasniedzejs(@PathVariable(name = "id") int id, Model model) {
		try {
			Pasniedzeji pasniedzejs = pasnService.retrieveById(id).getContent().getFirst();
			model.addAttribute("pasniedzejs", pasniedzejs);
			return "pasniedzeji-update-page";
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
			return "pasniedzeji-update-page";
		}
	}
	
	@PostMapping("/update/{id}")
	public String postControllerUpdatePasniedzejs(@PathVariable(name = "id") int id, Pasniedzeji pasniedzejs, Model model) {
		try {
			pasnService.updateById(id, pasniedzejs.getVards(), pasniedzejs.getUzvards(), pasniedzejs.getEpasts(), pasniedzejs.getTelefonaNr());
			return "redirect:/pasniedzeji/CRUD/show/all";
		} catch (Exception e) {
            e.printStackTrace();
			model.addAttribute("message", e.getMessage());
			return "pasniedzeji-update-page";
		}
	}
}
