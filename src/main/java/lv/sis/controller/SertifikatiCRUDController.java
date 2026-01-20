package lv.sis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import lv.sis.service.IFilterService;

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
	public String getControllerShowAllSertifikati(Model model, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "4") int size, @RequestParam(required = false) String search) {

		try {
			Page<Sertifikati> sertifikatiPage;

			if (search != null && !search.trim().isEmpty()) {
				List<Sertifikati> filtered = filterService.findByTipsContainingIgnoreCase(search.trim());

				sertifikatiPage = new PageImpl<>(filtered, PageRequest.of(0, size), filtered.size());

				model.addAttribute("search", search);
			} else {
				Pageable pageable = PageRequest.of(page, size);
				sertifikatiPage = sertService.retrieveAll(pageable);
			}

			model.addAttribute("sertifikati", sertifikatiPage);
			return "sertifikati-all-page";

		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}

	@GetMapping("/show/all/{id}")
	public String getControllerShowSertifikatsByID(@PathVariable(name = "id") Integer id, Model model) {
		try {
			Page<Sertifikati> sertifikats = sertService.retrieveById(id);
			model.addAttribute("sertifikati", sertifikats);
			return "sertifikati-one-page";
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}

	@GetMapping("/remove/{id}")
	public String getControllerRemoveSertifikats(@PathVariable(name = "id") int id, Model model,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "4") int size) {
		try {
			sertService.deleteById(id);
			Pageable pageable = PageRequest.of(page, size);
			model.addAttribute("sertifikati", sertService.retrieveAll(pageable));
			return "redirect:/sertifikati/CRUD/show/all?page=" + page + "&size=" + size;
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
	public String postControllerAddSertifikats(@ModelAttribute Sertifikati sertifikats, Model model,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "4") int size) {
		if (sertifikats == null) {
			model.addAttribute("package", "Sertifikats netika iedots");
		}

		try {
			System.out.println(sertifikats);
			sertService.create(sertifikats.getTips(), sertifikats.getIzdosanasDatums(), sertifikats.getCertificateNo(),
					sertifikats.isIrParakstits(), sertifikats.getDalibnieks(), sertifikats.getKurss());
			return "redirect:/sertifikati/CRUD/show/all?page=" + page + "&size=" + size;
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			e.printStackTrace();
			return "error-page";
		}
	}

	@GetMapping("/update/{id}")
	public String getControllerUpdateSertifikats(@PathVariable(name = "id") int id, Model model) {
		try {
			Sertifikati sertifikats = sertService.retrieveById(id).getContent().getFirst();
			model.addAttribute("sertifikats", sertifikats);
			return "sertifikats-update-page";
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}

	@PostMapping("/update/{id}")
	public String postControllerUpdateSertifikats(@PathVariable(name = "id") int id, Sertifikati sertifikats,
			Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "4") int size) {
		try {
			Sertifikati dbSertiikats = sertService.retrieveById(id).getContent().getFirst();
			
			dbSertiikats.setTips(sertifikats.getTips());
			dbSertiikats.setIzdosanasDatums(sertifikats.getIzdosanasDatums());
			dbSertiikats.setIrParakstits(sertifikats.isIrParakstits());
			dbSertiikats.setKurss(sertifikats.getKurss());
			
			sertService.save(dbSertiikats);
			
			return "redirect:/sertifikati/CRUD/show/all?page=" + page + "&size=" + size;
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			e.printStackTrace();
			return "error-page";
		}
	}
}