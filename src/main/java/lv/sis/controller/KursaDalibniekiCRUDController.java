package lv.sis.controller;

import java.util.List;

import jakarta.validation.Valid;
import lv.sis.service.IFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import lv.sis.model.KursaDalibnieki;
import lv.sis.service.ICRUDKursaDalibniekiService;

@Controller
@RequestMapping("kursaDalibnieki/CRUD")
public class KursaDalibniekiCRUDController {
    @Autowired
    private ICRUDKursaDalibniekiService kursaDalibniekiServiss;

    @Autowired
    private IFilterService filterService;

    @GetMapping("/show/all")
    public String getControllerShowAllKursaDalibnieki(Model model,
                                                      @RequestParam(required = false) String q,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "3") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);

            Page<KursaDalibnieki> kursaDal =
                    filterService.findDalibniekiByNameQuery(q, pageable);

            model.addAttribute("kursaDal", kursaDal);
            model.addAttribute("q", q); // lai input saglabā vērtību
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
            Page<KursaDalibnieki> page = new PageImpl<>(List.of(kursaDalibnieki), PageRequest.of(0, 1), 1);
            model.addAttribute("kursaDal", page);
            return "kursa-dalibnieki-all-page";
        } catch (Exception e) {
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/remove/{id}")
    public String getControllerRemoveKursaDalibnieku(@PathVariable(name = "id") int id, Model model) {
        try {
            KursaDalibnieki dalibnieks = kursaDalibniekiServiss.retrieveById(id);
            model.addAttribute("kursaDalibnieks", dalibnieks);
            return "kursa-dalibnieks-delete-confirm";
        } catch (Exception e) {
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }

    @PostMapping("/remove/{id}")
    public String deleteConfirmed(@PathVariable(name = "id") int id) {
        try {
            kursaDalibniekiServiss.deleteById(id);
            return "redirect:/kursaDalibnieki/CRUD/show/all";
        } catch (Exception e) {
            e.printStackTrace();
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
    public String postControllerAddKursaDalibnieku(
            @Valid @ModelAttribute("KursaDalibnieki") KursaDalibnieki kursaDalibnieki,
            BindingResult br,
            Model model) {

        if (br.hasErrors()) {
            model.addAttribute("message", br.getAllErrors().get(0).getDefaultMessage());
            return "kursa-dalibnieki-add-page";
        }

        try {
            kursaDalibniekiServiss.create(kursaDalibnieki.getVards(), kursaDalibnieki.getUzvards(),
                    kursaDalibnieki.getEpasts(), kursaDalibnieki.getTelefonaNr(), kursaDalibnieki.getPersonasId(),
                    kursaDalibnieki.getPilseta(), kursaDalibnieki.getValsts(),
                    kursaDalibnieki.getIelasNosaukumsNumurs(), kursaDalibnieki.getDzivoklaNr(),
                    kursaDalibnieki.getPastaIndekss());
            return "redirect:/kursaDalibnieki/CRUD/show/all";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", e.getMessage());
            return "kursa-dalibnieki-add-page";
        }
    }

    @GetMapping("/update/{id}")
    public String getControllerUpdateKursaDalibnieks(@PathVariable(name = "id") int id, Model model) {
        try {
            KursaDalibnieki kursaDalibnieki = kursaDalibniekiServiss.retrieveById(id);
            model.addAttribute("kursaDalibnieki", kursaDalibnieki);
            model.addAttribute("id", id);
            return "kursa-dalibnieki-update-page";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "kursa-dalibnieki-update-page";
        }
    }

    @PostMapping("/update/{id}")
    public String postControllerUpdateDoctor(
            @PathVariable(name = "id") int id,
            @Valid @ModelAttribute("kursaDalibnieki") KursaDalibnieki kursaDalibnieki,
            BindingResult br,
            Model model) {

        if (br.hasErrors()) {
            model.addAttribute("id", id);
            model.addAttribute("message", br.getAllErrors().get(0).getDefaultMessage());
            return "kursa-dalibnieki-update-page";
        }

        try {
            kursaDalibniekiServiss.updateById(
                    id,
                    kursaDalibnieki.getVards(),
                    kursaDalibnieki.getUzvards(),
                    kursaDalibnieki.getEpasts(),
                    kursaDalibnieki.getTelefonaNr(),
                    kursaDalibnieki.getPersonasId(),
                    kursaDalibnieki.getPilseta(),
                    kursaDalibnieki.getValsts(),
                    kursaDalibnieki.getIelasNosaukumsNumurs(),
                    kursaDalibnieki.getDzivoklaNr(),
                    kursaDalibnieki.getPastaIndekss()
            );
            return "redirect:/kursaDalibnieki/CRUD/show/all/" + id;
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("id", id);
            model.addAttribute("message", e.getMessage());
            return "kursa-dalibnieki-update-page";
        }
    }

    @GetMapping("/import") //localhost:8080/kursaDalibnieki/CRUD/import
    public String getControllerImportCourseParticipants(Model model) {
        return "kursa-dalibnieki-import-page";
    }

    @PostMapping("/import")
    public String postControllerImportCourseParticipants(@RequestParam("file") MultipartFile file, Model model) {
        try {
            kursaDalibniekiServiss.importCourseParticipants(file);
            return "redirect:/kursaDalibnieki/CRUD/show/all";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", e.getMessage());
            return "kursa-dalibnieki-import-page";
        }
    }

}
