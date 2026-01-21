package lv.sis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import lv.sis.service.IFilterService;

@Controller
@RequestMapping("/pasniedzeji/CRUD")
public class PasniedzejiCRUDController {

    private static final Logger logger = LoggerFactory.getLogger(PasniedzejiCRUDController.class);

    @Autowired
    private ICRUDPasniedzejiService pasnService;

    @Autowired
    private IFilterService filterService;

    @GetMapping("/show/all")
    public String getControllerShowAllPasniedzeji(Model model, @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "3") int size, @RequestParam(required
                    = false) String nosaukums) {
        logger.info("Request to show all lecturers (page={}, size={}, nosaukums={})", page, size, nosaukums);

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Pasniedzeji> visiKursi;

            if (nosaukums != null && !nosaukums.trim().isEmpty()) {
                logger.info("Filtering lecturers by course name={}", nosaukums);
                visiKursi = filterService.retrieveByKurss(nosaukums, pageable);
                model.addAttribute("nosaukums", nosaukums);
            } else {
                visiKursi = pasnService.retrieveAll(pageable);
            }

            model.addAttribute("package", visiKursi);
            logger.info("Successfully retrieved lecturers list");
            return "pasniedzeji-all-page";
        } catch (Exception e) {
            logger.error("Failed to retrieve lecturers list", e);
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/show/all/{id}")
    public String getControllerShowPasniedzejsByID(@PathVariable Integer id, Model model) {
        logger.info("Request to retrieve lecturer by id={}", id);

        try {
            Pasniedzeji pasniedzejs = pasnService.retrieveById(id);
            model.addAttribute("package", pasniedzejs);
            logger.info("Successfully retrieved lecturer id={}", id);
            return "pasniedzeji-all-page";
        } catch (Exception e) {
            logger.error("Failed to retrieve lecturer id={}", id, e);
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/remove/{id}")
    public String getControllerRemovePasniedzejs(@PathVariable int id, Model model) {
        logger.info("Preparing delete confirmation for lecturer id={}", id);

        try {
            Pasniedzeji pasniedzejs = pasnService.retrieveById(id);
            model.addAttribute("pasniedzejs", pasniedzejs);
            return "pasniedzeji-delete-confirm";
        } catch (Exception e) {
            logger.error("Failed to prepare delete confirmation for lecturer id={}", id, e);
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }

    @PostMapping("/remove/{id}")
    public String deleteConfirmed(@PathVariable int id) {
        logger.warn("Delete confirmed for lecturer id={}", id);

        try {
            pasnService.deleteById(id);
            logger.info("lecturer deleted id={}", id);
            return "redirect:/pasniedzeji/CRUD/show/all";
        } catch (Exception e) {
            logger.error("Failed to delete lecturer id={}", id, e);
            return "error-page";
        }
    }

    @GetMapping("/add")
    public String getControllerAddPasniedzejs(Model model) {
        logger.info("Opening add lecturer form");

        model.addAttribute("pasniedzejs", new Pasniedzeji());
        return "pasniedzeji-add-page";
    }

    @PostMapping("/add")
    public String postControllerAddPasniedzejs(@ModelAttribute Pasniedzeji pasniedzejs, Model model) {
        logger.info("Request to add new lecturer");

        try {
            if (pasniedzejs == null) {
                logger.warn("lecturer object is null");
                model.addAttribute("package", "Pasniedzejs nav iedots");
            }
            pasnService.create(pasniedzejs.getVards(), pasniedzejs.getUzvards(),
                    pasniedzejs.getEpasts(), pasniedzejs.getTelefonaNr());
            logger.info("lecturer created successfully");
            return "redirect:/pasniedzeji/CRUD/show/all";
        } catch (Exception e) {
            logger.error("Failed to create lecturer", e);
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/update/{id}")
    public String getControllerUpdatePasniedzejs(@PathVariable int id, Model model) {
        logger.info("Opening update form for lecturer id={}", id);

        try {
            Pasniedzeji pasniedzejs = pasnService.retrieveById(id);
            model.addAttribute("pasniedzejs", pasniedzejs);
            return "pasniedzeji-update-page";
        } catch (Exception e) {
            logger.error("Failed to open update form for lecturer id={}", id, e);
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }

    @PostMapping("/update/{id}")
    public String postControllerUpdatePasniedzejs(@PathVariable int id, Pasniedzeji pasniedzejs, Model model) {
        logger.info("Request to update lecturer id={}", id);

        try {
            pasnService.updateById(id, pasniedzejs.getVards(), pasniedzejs.getUzvards(),
                    pasniedzejs.getEpasts(), pasniedzejs.getTelefonaNr());
            logger.info("lecturer updated id={}", id);
            return "redirect:/pasniedzeji/CRUD/show/all";
        } catch (Exception e) {
            logger.error("Failed to update lecturer id={}", id, e);
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }
}