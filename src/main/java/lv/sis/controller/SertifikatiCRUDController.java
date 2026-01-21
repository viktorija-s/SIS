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

import lv.sis.model.Sertifikati;
import lv.sis.model.enums.CertificateType;
import lv.sis.repo.IKurssRepo;
import lv.sis.repo.IKursaDalibniekiRepo;
import lv.sis.service.ICRUDSertifikatiService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("sertifikati/CRUD")
public class SertifikatiCRUDController {

    private static final Logger logger = LoggerFactory.getLogger(SertifikatiCRUDController.class);

    @Autowired
    private ICRUDSertifikatiService sertService;
    @Autowired
    private IKursaDalibniekiRepo dalibniekiRepo;
    @Autowired
    private IKurssRepo kurssRepo;

    @GetMapping("/show/all")
    public String getControllerShowAllSertifikati(Model model,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "4") int size) {

        logger.info("Request to show all certificates (page={}, size={})", page, size);

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Sertifikati> visiSertifikati = sertService.retrieveAll(pageable);
            model.addAttribute("sertifikati", visiSertifikati);
            logger.info("Successfully retrieved certificate list");
            return "sertifikati-all-page";
        } catch (Exception e) {
            logger.error("Failed to retrieve certificates", e);
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/show/all/{id}")
    public String getControllerShowSertifikatsByID(@PathVariable Integer id, Model model) {
        logger.info("Request to retrieve certificate by id={}", id);

        try {
            Sertifikati sertifikats = sertService.retrieveById(id);
            model.addAttribute("sertifikati", sertifikats);
            return "sertifikati-all-page";
        } catch (Exception e) {
            logger.error("Failed to retrieve certificate id={}", id, e);
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/remove/{id}")
    public String getControllerRemoveSertifikats(@PathVariable int id, Model model) {
        logger.info("Opening delete confirmation for certificate id={}", id);

        try {
            Sertifikati sertifikats = sertService.retrieveById(id);
            model.addAttribute("sertifikats", sertifikats);
            return "sertifikati-delete-confirm";
        } catch (Exception e) {
            logger.error("Failed to open delete confirmation for certificate id={}", id, e);
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }

    @PostMapping("/remove/{id}")
    public String deleteConfirmed(@PathVariable int id) {
        logger.warn("Delete confirmed for certificate id={}", id);

        try {
            sertService.deleteById(id);
            logger.info("Certificate deleted id={}", id);
            return "redirect:/sertifikati/CRUD/show/all";
        } catch (Exception e) {
            logger.error("Failed to delete certificate id={}", id, e);
            return "error-page";
        }
    }

    @GetMapping("/add")
    public String getControllerAddSertifikats(Model model) {
        logger.info("Opening add certificate form");

        Sertifikati sertifikats = new Sertifikati();
        model.addAttribute("tips", CertificateType.values());
        model.addAttribute("dalibnieki", dalibniekiRepo.findAll());
        model.addAttribute("kursi", kurssRepo.findAll());
        model.addAttribute("sertifikats", sertifikats);
        return "sertifikati-add-page";
    }

    @PostMapping("/add")
    public String postControllerAddSertifikats(@ModelAttribute Sertifikati sertifikats,
                                               Model model,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "4") int size) {

        logger.info("Request to create new certificate");

        if (sertifikats == null) {
            logger.warn("Certificate object is null");
            model.addAttribute("package", "Sertifikats netika iedots");
        }

        try {
            logger.debug("Certificate data: {}", sertifikats);
            sertService.create(
                    sertifikats.getTips(),
                    sertifikats.getIzdosanasDatums(),
                    sertifikats.getRegistracijasNr(),
                    sertifikats.isIrParakstits(),
                    sertifikats.getDalibnieks(),
                    sertifikats.getKurss());
            logger.info("Certificate created successfully");
            return "redirect:/sertifikati/CRUD/show/all?page=" + page + "&size=" + size;
        } catch (Exception e) {
            logger.error("Failed to create certificate", e);
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/update/{id}")
    public String getControllerUpdateSertifikats(@PathVariable int id, Model model) {
        logger.info("Opening update form for certificate id={}", id);

        try {
            Sertifikati sertifikats = sertService.retrieveById(id);
            model.addAttribute("sertifikats", sertifikats);
            return "sertifikats-update-page";
        } catch (Exception e) {
            logger.error("Failed to open update form for certificate id={}", id, e);
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }

    @PostMapping("/update/{id}")
    public String postControllerUpdateSertifikats(@PathVariable int id,
                                                  Sertifikati sertifikats,
                                                  Model model,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "4") int size) {

        logger.info("Request to update certificate id={}", id);

        try {
            sertService.updateById(
                    id,
                    sertifikats.getTips(),
                    sertifikats.getIzdosanasDatums(),
                    sertifikats.getRegistracijasNr(),
                    sertifikats.isIrParakstits());
            logger.info("Certificate updated id={}", id);
            return "redirect:/sertifikati/CRUD/show/all?page=" + page + "&size=" + size;
        } catch (Exception e) {
            logger.error("Failed to update certificate id={}", id, e);
            model.addAttribute("package", e.getMessage());
            return "error-page";
        }
    }
}