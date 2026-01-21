package lv.sis.controller;

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

import jakarta.validation.constraints.Min;
import lv.sis.model.KursaDalibnieki;
import lv.sis.model.KursaDatumi;
import lv.sis.model.Vertejumi;
import lv.sis.repo.IKursaDalibniekiRepo;
import lv.sis.repo.IKursaDatumiRepo;
import lv.sis.service.ICRUDVertejumiService;
import lv.sis.service.IFilterService;
import lv.sis.repo.IVertejumiRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("vertejumi/CRUD")
public class VertejumiCRUDController {

    private static final Logger logger = LoggerFactory.getLogger(VertejumiCRUDController.class);

    @Autowired
    private ICRUDVertejumiService vertejumiServiss;

    @Autowired
    private IKursaDalibniekiRepo kursaDalibniekiRepo;

    @Autowired
    private IKursaDatumiRepo kursaDatumiRepo;

    @Autowired
    IFilterService filterService;

    @GetMapping("/show/all")
    public String getControllerShowAllVertejumi(Model model, @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "3") int size, @RequestParam(required = false) Integer min,
                                                @RequestParam(required = false) Integer max) {

        logger.info("Request to show all grades (page={}, size={}, min={}, max={})", page, size, min, max);

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Vertejumi> visiVertejumi;

            if (min != null || max != null) {
                visiVertejumi = filterService.retrieveByMinMax(min, max, pageable);
                model.addAttribute("min", min);
                model.addAttribute("max", max);
            } else {
                visiVertejumi = vertejumiServiss.retrieveAll(pageable);
            }

            model.addAttribute("package", visiVertejumi);
            logger.info("Grades retrieved successfully");
            return "vertejumi-all-page";
        } catch (Exception e) {
            logger.error("Failed to retrieve grades", e);
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/show/all/{id}")
    public String getControllerShowVertejumsByID(@PathVariable(name = "id") Integer id, Model model) {
        logger.info("Request to retrieve grade id={}", id);
        try {
            Vertejumi vertejumi = vertejumiServiss.retrieveById(id);
            model.addAttribute("package", vertejumi);
            return "vertejumi-all-page";
        } catch (Exception e) {
            logger.error("Failed to retrieve grade id={}", id, e);
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/remove/{id}")
    public String getControllerRemoveVertejums(@PathVariable(name = "id") int id, Model model) {
        logger.info("Opening delete confirmation for grade id={}", id);
        try {
            Vertejumi vertejumi = vertejumiServiss.retrieveById(id);
            model.addAttribute("vertejumi", vertejumi);
            return "vertejumi-delete-confirm";
        } catch (Exception e) {
            logger.error("Failed to open delete confirmation for grade id={}", id, e);
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }

    @PostMapping("/remove/{id}")
    public String deleteConfirmed(@PathVariable(name = "id") int id) {
        logger.warn("Delete confirmed for grade id={}", id);
        try {
            vertejumiServiss.deleteById(id);
            logger.info("Grade deleted id={}", id);
            return "redirect:/vertejumi/CRUD/show/all";
        } catch (Exception e) {
            logger.error("Failed to delete grade id={}", id, e);
            return "error-page";
        }
    }

    @GetMapping("/add")
    public String getControllerAddVertejums(Model model) {
        logger.info("Opening add grade form");

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
            logger.warn("Grade object is null");
            model.addAttribute("package", "The vertejumi is not given");
        }
        try {
            logger.info("Creating new grade");
            vertejumiServiss.create(
                    vertejumi.getVertejums(),
                    vertejumi.getDatums(),
                    vertejumi.getKursaDalibnieki(),
                    vertejumi.getKursaDatumi()
            );
            logger.info("Grade created successfully");
            return "redirect:/vertejumi/CRUD/show/all";
        } catch (Exception e) {
            logger.error("Failed to create grade", e);
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/update/{id}")
    public String getControllerUpdateVertejums(@PathVariable(name = "id") int id, Model model) {
        logger.info("Opening update form for grade id={}", id);
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
            logger.error("Failed to open update form for grade id={}", id, e);
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }

    @PostMapping("/update/{id}")
    public String postControllerUpdateVertejums(@PathVariable(name = "id") int id, Vertejumi vertejumi, Model model) {
        logger.info("Updating grade id={}", id);
        try {
            vertejumiServiss.updateById(id, vertejumi.getVertejums());
            logger.info("Grade updated id={}", id);
            return "redirect:/vertejumi/CRUD/show/all";
        } catch (Exception e) {
            logger.error("Failed to update grade id={}", id, e);
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }
}
