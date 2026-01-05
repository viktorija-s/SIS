package lv.sis.controller;

import java.util.ArrayList;

import lv.sis.service.IFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/show/all")
    public String getControllerShowAllKursi(
            @RequestParam(required = false) String search,
            Model model) {
        try {
            ArrayList<Kurss> kursi;
            if (search != null && !search.trim().isEmpty()) {
                kursi = filterService.findByNosaukumsContainingIgnoreCase(search.trim());
                model.addAttribute("search", search);
            } else {
                kursi = kurssService.retrieveAll();
            }

            model.addAttribute("package", kursi);
            return "kurss-all-page";

        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "kurss-all-page";
        }
    }

    @GetMapping("/show/all/{id}")
    public String getControllerShowKurssByID(@PathVariable(name = "id") Integer id, Model model) {
        try {
            Kurss kurss = kurssService.retrieveById(id);
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
            kurssService.deleteById(id);
            model.addAttribute("package", kurssService.retrieveAll());
            return "kurss-all-page";
        } catch (Exception e) {
            model.addAttribute("package", e.getMessage());
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
            Kurss kurss = kurssService.retrieveById(id);
            model.addAttribute("kurss", kurss);
            return "kurss-update-page";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "kurss-update-page";
        }
    }

    @PostMapping("/update/{id}")
    public String postControllerUpdateKurss(@PathVariable(name = "id") int id, Kurss kurss, Model model) {
        try {
            kurssService.updateById(id, kurss.getNosaukums(), kurss.getStundas(), kurss.getLimenis());
            return "redirect:/kurss/CRUD/show/all";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", e.getMessage());
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
