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

import lv.sis.model.Vertejumi;
import lv.sis.service.ICRUDVertejumiService;

@Controller
@RequestMapping("vertejumi/CRUD")
public class VertejumiCRUDController {
	@Autowired
	private ICRUDVertejumiService vertejumiServiss;

	@GetMapping("/show/all")
	public String getControllerShowAllVertejumi(Model model) {
		try {
			ArrayList<Vertejumi> visiVertejumi = vertejumiServiss.retrieveAll();
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
			return "vertejumi-all-page";
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}

	@GetMapping("/remove/{id}")
	public String getControllerRemoveVertejums(@PathVariable(name = "id") int id, Model model) {
		try {
			vertejumiServiss.deleteById(id);
			model.addAttribute("package", vertejumiServiss.retrieveAll());
			return "vertejumi-all-page";
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}

	@GetMapping("/add")
	public String getControllerAddVertejums(Model model) {
	    Vertejumi vertejumi = new Vertejumi();
	    model.addAttribute("vertejumi", vertejumi);
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
