package lv.sis.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import jakarta.validation.Valid;
import lv.sis.model.Sertifikati;
import lv.sis.model.enums.CertificateType;
import lv.sis.repo.IKurssRepo;
import lv.sis.repo.IKursaDalibniekiRepo;
import lv.sis.service.ICRUDSertifikatiService;
import lv.sis.service.IFilterService;

@Controller
@RequestMapping("sertifikati/CRUD")
public class SertifikatiCRUDController {

	private final ICRUDSertifikatiService sertService;
	private final IKursaDalibniekiRepo dalibniekiRepo;
	private final IKurssRepo kurssRepo;
	private final IFilterService filterService;
	
	
	private static final String ATTR_SERTIFIKATI = "sertifikati";
	private static final String ATTR_ERROR = "package";

	private static final String VIEW_ERROR = "error-page";
	private static final String VIEW_ALL = "sertifikati-all-page";
	private static final String VIEW_ONE = "sertifikati-one-page";

	private static final String REDIRECT_SHOW_ALL = "redirect:/sertifikati/CRUD/show/all?page=";
	private static final String PARAM_SIZE = "&size=";
	
	private static final String ATTR_DALIBNIEKI = "dalibnieki";
	private static final String ATTR_KURSI = "kursi";
	private static final String ATTR_SERTIFIKATS = "sertifikats";
	private static final String ATTR_TIPS = "tips";

	private static final String VIEW_ADD = "sertifikati-add-page";
	private static final String VIEW_UPDATE = "sertifikats-update-page";

	private static final Logger logger =
	        LoggerFactory.getLogger(SertifikatiCRUDController.class);


	public SertifikatiCRUDController(ICRUDSertifikatiService sertService, IKursaDalibniekiRepo dalibniekiRepo,
			IKurssRepo kurssRepo, IFilterService filterService) {

		this.sertService = sertService;
		this.dalibniekiRepo = dalibniekiRepo;
		this.kurssRepo = kurssRepo;
		this.filterService = filterService;
	}

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

			model.addAttribute(ATTR_SERTIFIKATI, sertifikatiPage);
			return VIEW_ALL;

		} catch (Exception e) {
			model.addAttribute(ATTR_ERROR, e.getMessage());
			return VIEW_ERROR;
		}
	}

	@GetMapping("/show/all/{id}")
	public String getControllerShowSertifikatsByID(@PathVariable(name = "id") Integer id, Model model) {
		try {
			Page<Sertifikati> sertifikats = sertService.retrieveById(id);
			model.addAttribute(ATTR_SERTIFIKATI, sertifikats);
			return VIEW_ONE;
		} catch (Exception e) {
			model.addAttribute(ATTR_ERROR, e.getMessage());
			return VIEW_ERROR;
		}
	}

	@GetMapping("/remove/{id}")
	public String getControllerRemoveSertifikats(@PathVariable(name = "id") int id, Model model,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "4") int size) {
		try {
			sertService.deleteById(id);
			Pageable pageable = PageRequest.of(page, size);
			model.addAttribute(ATTR_SERTIFIKATI, sertService.retrieveAll(pageable));
			return REDIRECT_SHOW_ALL + page + PARAM_SIZE + size;
		} catch (Exception e) {
			model.addAttribute(ATTR_ERROR, e.getMessage());
			return VIEW_ERROR;
		}
	}

	@GetMapping("/add")
	public String getControllerAddSertifikats(Model model) {
		Sertifikati sertifikats = new Sertifikati();
		model.addAttribute(ATTR_TIPS, CertificateType.values());
		model.addAttribute(ATTR_DALIBNIEKI, dalibniekiRepo.findAll());
		model.addAttribute(ATTR_KURSI, kurssRepo.findAll());
		model.addAttribute(ATTR_SERTIFIKATS, sertifikats);
		return VIEW_ADD;
	}

	@PostMapping("/add")
	public String postControllerAddSertifikats(@ModelAttribute Sertifikati sertifikats, Model model,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "4") int size) {
		if (sertifikats == null) {
			model.addAttribute(ATTR_ERROR, "Sertifikats netika iedots");
			return VIEW_ERROR;
		}

		try {
			logger.info("Creating sertifikats: {}", sertifikats);
			sertService.create(sertifikats.getTips(), sertifikats.getIzdosanasDatums(), sertifikats.getCertificateNo(),
					sertifikats.isIrParakstits(), sertifikats.getDalibnieks(), sertifikats.getKurss());
			return REDIRECT_SHOW_ALL + page + PARAM_SIZE + size;
		} catch (Exception e) {
			model.addAttribute(ATTR_ERROR, e.getMessage());
			logger.error("Error while creating sertifikats", e);
			return VIEW_ERROR;
		}
	}

	@GetMapping("/update/{id}")
	public String getControllerUpdateSertifikats(@PathVariable int id, Model model) {
		try {
			Sertifikati found = sertService.retrieveById(id).getContent().getFirst();

			model.addAttribute(ATTR_SERTIFIKATS, found);
			model.addAttribute(ATTR_KURSI, kurssRepo.findAll());
			model.addAttribute(ATTR_DALIBNIEKI, dalibniekiRepo.findAll());

			return VIEW_UPDATE;
		} catch (Exception e) {
			model.addAttribute(ATTR_ERROR, e.getMessage());
			return VIEW_ERROR;
		}
	}

	@PostMapping("/update/{id}")
	public String postControllerUpdateSertifikats(@PathVariable int id, @Valid Sertifikati sertifikats,
			BindingResult result, @RequestParam int kid, @RequestParam int kdid, Model model,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "4") int size) {

		if (result.hasErrors()) {
			model.addAttribute(ATTR_KURSI, kurssRepo.findAll());
			model.addAttribute(ATTR_DALIBNIEKI, dalibniekiRepo.findAll());
			return VIEW_UPDATE;
		}

		try {
			Sertifikati db = sertService.retrieveById(id).getContent().getFirst();

			db.setTips(sertifikats.getTips());
			db.setIzdosanasDatums(sertifikats.getIzdosanasDatums());
			db.setIrParakstits(sertifikats.isIrParakstits());

			db.setKurss(kurssRepo.findById(kid).orElseThrow());
			db.setDalibnieks(dalibniekiRepo.findById(kdid).orElseThrow());

			sertService.save(db);

			return REDIRECT_SHOW_ALL + page + PARAM_SIZE + size;

		} catch (Exception e) {
			model.addAttribute(ATTR_ERROR, e.getMessage());
			return VIEW_ERROR;
		}
	}

}