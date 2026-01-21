package lv.sis.controller;


import lv.sis.service.IFilterService;

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

import lv.sis.model.Kurss;
import lv.sis.model.enums.Limeni;
import lv.sis.service.ICRUDKurssService;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("kurss/CRUD")
public class KurssCRUDController {
    @Autowired
    private ICRUDKurssService kurssService;

    @Autowired
    private IFilterService filterService;

    @GetMapping("/show/all") // localhost:8080/kurss/CRUD/show/all?page=0&size=3
    public String getControllerShowAllKursi(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            Model model) {

        try {
            if (search != null && !search.trim().isEmpty()) {
                Page<Kurss> kursi =
                        filterService.findByNosaukumsContainingIgnoreCase(search.trim());

                model.addAttribute("search", search);
                model.addAttribute("package", kursi);

			}
			else {
				Pageable pageable = PageRequest.of(page, size);
				Page<Kurss> visiKursi = kurssService.retrieveAll(pageable); 
				model.addAttribute("package", visiKursi);
			}
			return "kurss-all-page"; 
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}

    @GetMapping("/show/all/{id}")
    public String getControllerShowKurssByID(@PathVariable(name = "id") Integer id, Model model) {
        try {
            Page<Kurss> kurss = kurssService.retrieveById(id);
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
            Kurss kurss = kurssService.retrieveById(id).getContent().getFirst();
            model.addAttribute("kurss", kurss);
            return "kurss-delete-confirm";
        } catch (Exception e) {
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }

    @PostMapping("/remove/{id}")
    public String deleteConfirmed(@PathVariable(name = "id") int id) {
        try {
            kurssService.deleteById(id);
            return "redirect:/kurss/CRUD/show/all";
        } catch (Exception e) {
            e.printStackTrace();
            return "error-page";
        }
    }

    @GetMapping("/add")
    public String getControllerAddKurss(Model model) {
        model.addAttribute("kurss", new Kurss());
        model.addAttribute("limeni", Limeni.values());
        return "kurss-add-page";
    }

    @PostMapping("/add")
    public String postControllerAddKurss(@ModelAttribute Kurss kurss, Model model) {
        if (kurss == null || kurss.getStundas() < 1) {
            model.addAttribute("message", "Nav ievadīts kursa nosaukums vai stundas!");
            model.addAttribute("limeni", Limeni.values());
            return "kurss-add-page";
        }

        try {
            kurssService.create(kurss.getNosaukums(), kurss.getStundas(), kurss.getLimenis());
            model.addAttribute("kurss", new Kurss());
            model.addAttribute("limeni", Limeni.values());
            return "redirect:/kurss/CRUD/show/all";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", e.getMessage());
            model.addAttribute("limeni", Limeni.values());
            return "kurss-add-page";
        }
    }

    @GetMapping("/update/{id}")
    public String getControllerUpdateKurss(@PathVariable(name = "id") int id, Model model) {
        try {
        	Kurss kurss = kurssService.retrieveById(id).getContent().getFirst();
            model.addAttribute("kurss", kurss);
            model.addAttribute("limeni", Limeni.values()); // added 
            model.addAttribute("id", id);
            return "kurss-update-page";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "kurss-update-page";
        }
    }

    @PostMapping("/update/{id}")
    public String postControllerUpdateKurss(@PathVariable(name = "id") int id, Kurss kurss, Model model) {
        if (kurss == null || kurss.getStundas() < 1) {
            model.addAttribute("message", "Nav ievadīts kursa nosaukums vai stundas!");
            model.addAttribute("limeni", Limeni.values());
            return "kurss-update-page";
        }

        try {
            if (kurss.getLimenis() == null) {
                Kurss existing = kurssService.retrieveById(id).getContent().getFirst();
                kurss.setLimenis(existing.getLimenis());
            }

            kurssService.updateById(
                    id,
                    kurss.getNosaukums(),
                    kurss.getStundas(),
                    kurss.getLimenis()
            );
            return "redirect:/kurss/CRUD/show/all";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", e.getMessage());
            model.addAttribute("limeni", Limeni.values());
            return "kurss-update-page";
        }
    }

    @GetMapping("/import")
    public String getControllerImportCourses() {
        return "kurss-import-page";
    }

    @PostMapping("/import")
    public String postControllerImportCourses(@RequestParam("file") MultipartFile file, Model model) {
        try {
            kurssService.importCourses(file);
            return "redirect:/kurss/CRUD/show/all";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", e.getMessage());
            return "kurss-import-page";
        }
    }

}

