package lv.sis.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lv.sis.service.IFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import lv.sis.model.KursaDatumi;
import lv.sis.model.Kurss;
import lv.sis.model.Pasniedzeji;
import lv.sis.repo.IKurssRepo;
import lv.sis.repo.IPasniedzejiRepo;
import lv.sis.service.ICRUDKursaDatumiService;

@Controller
@RequestMapping("kursaDatumi/CRUD")
public class KursaDatumiCRUDController {

	@Autowired
	private ICRUDKursaDatumiService kursaDatumiService;
	
	@Autowired
	private IPasniedzejiRepo pasniedzejiRepo;
	
	@Autowired
	private IKurssRepo kurssRepo;

    @Autowired
    private IFilterService filterService;

    @GetMapping("/show/all")
    public String getControllerShowAllKursaDatumi(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,

            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {

        try {
            if (from != null && to != null) {
            	
                Page<KursaDatumi> kursaDatumi =
                        filterService.findKursaDatumiBetweenDates(from, to);

                model.addAttribute("from", from);
                model.addAttribute("to", to);
                model.addAttribute("package", kursaDatumi);
            } else {
                Pageable pageable = PageRequest.of(page, size);
                Page<KursaDatumi> visiKursaDatumi =
                        kursaDatumiService.retrieveAll(pageable);

                model.addAttribute("package", visiKursaDatumi);
            }

            return "kursa-datumi-all-page";

        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "kursa-datumi-all-page";
        }
    }

	
	@GetMapping("/show/all/{id}")
	public String getControllerShowKursaDatumsByID(@PathVariable(name = "id") Integer id, Model model) {
		try {
			Page<KursaDatumi> kursaDatumi = kursaDatumiService.retrieveById(id);
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
			KursaDatumi kursaDatumi = kursaDatumiService.retrieveById(id).getContent().getFirst();
			model.addAttribute("kursaDatumi", kursaDatumi);
			return "kursa-datumi-delete-confirm";
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}

	@PostMapping("/remove/{id}")
	public String deleteConfirmed(@PathVariable(name = "id") int id) {
		try {
			kursaDatumiService.deleteById(id);
			return "redirect:/kursaDatumi/CRUD/show/all";
		} catch (Exception e) {
			e.printStackTrace();
			return "error-page";
		}
	}
	
	@GetMapping("/add")
    public String getControllerAddKursaDatumi(Model model) {
        KursaDatumi kursaDatumi = new KursaDatumi();
        model.addAttribute("kursaDatumi", kursaDatumi);
        
        List<Kurss> kurssList = kurssRepo.findAll();
        model.addAttribute("kurssList", kurssList);

        List<Pasniedzeji> pasniedzejiList = new ArrayList<>();
        pasniedzejiRepo.findAll().forEach(pasniedzejiList::add);
        model.addAttribute("pasniedzejiList", pasniedzejiList);
        
        return "kursa-datumi-add-page";
    }

    @PostMapping("/add")
    public String postControllerAddKursaDatumi(@ModelAttribute KursaDatumi kursaDatumi, Model model) {
        if (kursaDatumi == null) {
            model.addAttribute("message", "The kursa datumi is not given");
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
            e.printStackTrace();
            model.addAttribute("message", e.getMessage());

            model.addAttribute("kurssList", kurssRepo.findAll());

            List<Pasniedzeji> pasniedzejiList = new ArrayList<>();
            pasniedzejiRepo.findAll().forEach(pasniedzejiList::add);
            model.addAttribute("pasniedzejiList", pasniedzejiList);

            return "kursa-datumi-add-page";
        }
    }

    @GetMapping("/update/{id}")
    public String getControllerUpdateKursaDatumi(@PathVariable(name = "id") int id, Model model) {
        try {
            KursaDatumi kursaDatumi = kursaDatumiService.retrieveById(id).getContent().getFirst();
            model.addAttribute("kursaDatumi", kursaDatumi);

            model.addAttribute("kurssList", kurssRepo.findAll());

            List<Pasniedzeji> pasniedzejiList = new ArrayList<>();
            pasniedzejiRepo.findAll().forEach(pasniedzejiList::add);
            model.addAttribute("pasniedzejiList", pasniedzejiList);

            return "kursa-datumi-update-page";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "kursa-datumi-update-page";
        }
    }

    @PostMapping("/update/{id}")
    public String postControllerUpdateKursaDatumi(@PathVariable(name = "id") int id,
                                                  @ModelAttribute KursaDatumi kursaDatumi,
                                                  Model model) {
        try {
            KursaDatumi existing = kursaDatumiService.retrieveById(id).getContent().getFirst();

            if (kursaDatumi.getKurss() == null) {
                kursaDatumi.setKurss(existing.getKurss());
            }
            if (kursaDatumi.getPasniedzejs() == null) {
                kursaDatumi.setPasniedzejs(existing.getPasniedzejs());
            }
            if (kursaDatumi.getSakumaDatums() == null) {
                kursaDatumi.setSakumaDatums(existing.getSakumaDatums());
            }
            if (kursaDatumi.getBeiguDatums() == null) {
                kursaDatumi.setBeiguDatums(existing.getBeiguDatums());
            }

            kursaDatumiService.updateById(id, kursaDatumi);
            return "redirect:/kursaDatumi/CRUD/show/all";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", e.getMessage());

            model.addAttribute("kurssList", kurssRepo.findAll());

            List<Pasniedzeji> pasniedzejiList = new ArrayList<>();
            pasniedzejiRepo.findAll().forEach(pasniedzejiList::add);
            model.addAttribute("pasniedzejiList", pasniedzejiList);

            return "kursa-datumi-update-page";
        }
    }
}
