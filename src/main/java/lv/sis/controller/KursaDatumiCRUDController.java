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

import lv.sis.model.KursaDatumi;
import lv.sis.service.ICRUDKursaDatumiService;

@Controller
@RequestMapping("kursaDatumi/CRUD")
public class KursaDatumiCRUDController {

	@Autowired
	private ICRUDKursaDatumiService kursaDatumiService;
	
	@GetMapping("/show/all")
	public String getControllerShowAllKursaDatumi(Model model) {
		try {
			ArrayList<KursaDatumi> visiKursaDatumi = kursaDatumiService.retrieveAll(); 
			model.addAttribute("package", visiKursaDatumi);
			return "kursa-datumi-all-page";
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}
	
	@GetMapping("/show/all/{id}")
	public String getControllerShowKursaDatumsByID(@PathVariable(name = "id") Integer id, Model model) {
		try {
			KursaDatumi kursaDatumi = kursaDatumiService.retrieveById(id);
			model.addAttribute("package", kursaDatumi);
			return "kursa-datumi-all-page";
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}
	
	@GetMapping("/remove/{id}")
	public String getControllerRemoveKursaDatumi(@PathVariable(name = "id") int id, Model model) {
        try {
            kursaDatumiService.deleteById(id);
            model.addAttribute("package", kursaDatumiService.retrieveAll());
            return "kursa-datumi-all-page";
        } catch (Exception e) {
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }
	
	@GetMapping("/add")
    public String getControllerAddKursaDatumi(Model model) {
        KursaDatumi kursaDatumi = new KursaDatumi();
        model.addAttribute("kursaDatumi", kursaDatumi);
        return "kursa-datumi-add-page";
    }
	
	@PostMapping("/add")
    public String postControllerAddKursaDatumi(@ModelAttribute KursaDatumi kursaDatumi, Model model) {
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
            return "redirect:/kursaDatumi/CRUD/show/all";
        } catch (Exception e) {
            model.addAttribute("package", e.getMessage());
            e.printStackTrace();
            return "error-page";
        }
    }
	
	@GetMapping("/update/{id}")
    public String getControllerUpdateKursaDatumi(@PathVariable(name = "id") int id, Model model) {
        try {
            KursaDatumi kursaDatumi = kursaDatumiService.retrieveById(id);
            model.addAttribute("kursaDatumi", kursaDatumi);
            return "kursa-datumi-update-page";
        } catch (Exception e) {
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }
	
	 @PostMapping("/update/{id}")
	 public String postControllerUpdateKursaDatumi(@PathVariable(name = "id") int id, KursaDatumi kursaDatumi, Model model) {
	     try {
	         kursaDatumiService.updateById(id, kursaDatumi);
	         return "redirect:/kursadatumi/CRUD/show/all";
	     } catch (Exception e) {
	         model.addAttribute("package", e.getMessage());
	         e.printStackTrace();
	         return "error-page";
	     }
	}
	
}
