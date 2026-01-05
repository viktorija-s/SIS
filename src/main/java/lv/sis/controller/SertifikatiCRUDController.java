package lv.sis.controller;

import java.util.ArrayList;

import lv.sis.service.IFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import lv.sis.model.Sertifikati;
import lv.sis.model.enums.CertificateType;
import lv.sis.repo.IKurssRepo;
import lv.sis.repo.IKursaDalibniekiRepo;
import lv.sis.service.ICRUDSertifikatiService;

@Controller
@RequestMapping("sertifikati/CRUD")
public class SertifikatiCRUDController {

    @Autowired
    private ICRUDSertifikatiService sertService;

    @Autowired
    private IKursaDalibniekiRepo dalibniekiRepo;

    @Autowired
    private IKurssRepo kurssRepo;

    @Autowired
    private IFilterService filterService;

    @GetMapping("/show/all")
    public String getControllerShowAllSertifikati(@RequestParam(required = false) String vards,
                                                  @RequestParam(required = false) String uzvards, Model model) {
        try {
            ArrayList<Sertifikati> sertifikati;
            if ((vards != null && !vards.isBlank()) ||
                    (uzvards != null && !uzvards.isBlank())) {
                sertifikati = filterService.findSertifikatiByDalibniekaVardsUzvards(vards, uzvards);
                model.addAttribute("vards", vards);
                model.addAttribute("uzvards", uzvards);
            } else {
                sertifikati = sertService.retrieveAll();
            }
            model.addAttribute("package", sertifikati);
            return "sertifikati-all-page";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "sertifikati-all-page";
        }
    }

    @GetMapping("/show/all/{id}")
    public String getControllerShowSertifikatsByID(@PathVariable(name = "id") Integer id, Model model) {
        try {
            Sertifikati sertifikats = sertService.retrieveById(id);
            model.addAttribute("package", sertifikats);
            return "sertifikati-all-page";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "sertifikati-all-page";
        }
    }

    @GetMapping("/remove/{id}")
    public String getControllerRemoveSertifikats(@PathVariable(name = "id") int id, Model model) {
        try {
            sertService.deleteById(id);
            model.addAttribute("package", sertService.retrieveAll());
            return "sertifikati-all-page";
        } catch (Exception e) {
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/add")
    public String getControllerAddSertifikats(Model model) {
        Sertifikati sertifikats = new Sertifikati();
        model.addAttribute("tips", CertificateType.values());
        model.addAttribute("dalibnieki", dalibniekiRepo.findAll());
        model.addAttribute("kursi", kurssRepo.findAll());
        model.addAttribute("sertifikats", sertifikats);
        return "sertifikati-add-page";
    }

    @PostMapping("/add")
    public String postControllerAddSertifikats(@ModelAttribute Sertifikati sertifikats, Model model) {
        if (sertifikats == null) {
            model.addAttribute("package", "Sertifikats netika iedots");
        }

        try {
            sertService.create(sertifikats.getTips(), sertifikats.getIzdosanasDatums(),
                    sertifikats.getRegistracijasNr(), sertifikats.isIrParakstits(), sertifikats.getDalibnieks(),
                    sertifikats.getKurss());
            return "redirect:/sertifikati/CRUD/show/all";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            e.printStackTrace();
            return "sertifikati-add-page";
        }
    }

    @GetMapping("/update/{id}")
    public String getControllerUpdateSertifikats(@PathVariable(name = "id") int id, Model model) {
        try {
            Sertifikati sertifikats = sertService.retrieveById(id);
            model.addAttribute("sertifikats", sertifikats);
            return "sertifikats-update-page";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", e.getMessage());
            return "sertifikats-update-page";
        }
    }

    @PostMapping("/update/{id}")
    public String postControllerUpdateSertifikats(@PathVariable(name = "id") int id, Sertifikati sertifikats,
                                                  Model model) {
        try {
            sertService.updateById(id, sertifikats.getTips(), sertifikats.getIzdosanasDatums(),
                    sertifikats.getRegistracijasNr(), sertifikats.isIrParakstits());
            return "redirect:/sertifikati/CRUD/show/all";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            e.printStackTrace();
            return "sertifikats-update-page";
        }
    }
}
