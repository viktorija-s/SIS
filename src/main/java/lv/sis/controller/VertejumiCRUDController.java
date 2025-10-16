package lv.sis.controller;

import java.time.LocalDate;
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

import lv.sis.model.KursaDalibnieki;
import lv.sis.model.KursaDatumi;
import lv.sis.model.Vertejumi;
import lv.sis.repo.IKursaDalibniekiRepo;
import lv.sis.repo.IKursaDatumiRepo;
import lv.sis.service.ICRUDVertejumiService;

@Controller
@RequestMapping("vertejumi/CRUD")
public class VertejumiCRUDController {
	@Autowired
	private ICRUDVertejumiService vertejumiServiss;
	
	@Autowired 
	private IKursaDalibniekiRepo kursaDalibniekiRepo;
	
	@Autowired 
	private IKursaDatumiRepo kursaDatumiRepo;

	@GetMapping("/show/all")
	public String getControllerShowAllVertejumi(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) {
		try {
			Pageable pageable = PageRequest.of(page, size);
			Page<Vertejumi> visiVertejumi = vertejumiServiss.retrieveAll(pageable); 

			model.addAttribute("package", visiVertejumi);
			return "vertejumi-all-page";
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}

	@GetMapping("/show/all/{id}")
	public String getControllerShowVertejumsByID(@PathVariable(name = "id") Integer id, Model model) {
		try {
			Vertejumi vertejumi = vertejumiServiss.retrieveById(id);
			model.addAttribute("package", vertejumi);
			return "vertejumi-one-page";
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}

	@GetMapping("/remove/{id}")
	public String getControllerRemoveVertejums(@PathVariable(name = "id") int id, Model model) {
		try {
			vertejumiServiss.deleteById(id);
			return "redirect:/vertejumi/CRUD/show/all";
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}

	@GetMapping("/add")
	public String getControllerAddVertejums(Model model) {
	    Vertejumi vertejumi = new Vertejumi();
	    model.addAttribute("vertejumi", vertejumi);
	    
	    
	    List<KursaDalibnieki> kursaDalibniekiList = new ArrayList<>();
	    kursaDalibniekiRepo.findAll().forEach(kursaDalibniekiList::add);
	    model.addAttribute("kursaDalibniekiList", kursaDalibniekiList);

	    List<KursaDatumi> kursaDatumiList = new ArrayList<>();
	    kursaDatumiRepo.findAll().forEach(kursaDatumiList::add);
	    model.addAttribute("kursaDatumiList", kursaDatumiList);
	    
	    return "vertejumi-add-page";
	}

	@PostMapping("/add")
	public String postControllerAddVertejums(@ModelAttribute Vertejumi vertejumi, Model model) {
		if (vertejumi == null) {
			model.addAttribute("package", "The vertejumi is not given");
			
		
		}
		try {
			vertejumiServiss.create(vertejumi.getVertejums(), vertejumi.getDatums(), vertejumi.getKursaDalibnieki(),
					vertejumi.getKursaDatumi());
			return "redirect:/vertejumi/CRUD/show/all";
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			e.printStackTrace();
			return "error-page";
		}
	}

	@GetMapping("/update/{id}")
	public String getControllerUpdateVertejums(@PathVariable(name = "id") int id, Model model) {
		try {
			Vertejumi vertejumi = vertejumiServiss.retrieveById(id);
			model.addAttribute("vertejumi", vertejumi);
			
			List<KursaDalibnieki> kursaDalibniekiList = new ArrayList<>();
		    kursaDalibniekiRepo.findAll().forEach(kursaDalibniekiList::add);
		    model.addAttribute("kursaDalibniekiList", kursaDalibniekiList);

		    List<KursaDatumi> kursaDatumiList = new ArrayList<>();
		    kursaDatumiRepo.findAll().forEach(kursaDatumiList::add);
		    model.addAttribute("kursaDatumiList", kursaDatumiList);
			
			return "vertejumi-update-page";
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}

	@PostMapping("/update/{id}")
	public String postControllerUpdateVertejums(@PathVariable(name = "id") int id, Vertejumi vertejumi, Model model) {
		try {
			vertejumiServiss.updateById(id, vertejumi.getVertejums());
			return "redirect:/vertejumi/CRUD/show/all";
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			e.printStackTrace();
			return "error-page";
		}
	}
}
