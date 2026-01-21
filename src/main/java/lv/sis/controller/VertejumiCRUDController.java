package lv.sis.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import lv.sis.model.KursaDalibnieki;
import lv.sis.model.KursaDatumi;
import lv.sis.model.Vertejumi;
import lv.sis.repo.IKursaDalibniekiRepo;
import lv.sis.repo.IKursaDatumiRepo;
import lv.sis.service.ICRUDVertejumiService;
import lv.sis.service.IFilterService;

@Controller
@RequestMapping("vertejumi/CRUD")
public class VertejumiCRUDController {

	private final ICRUDVertejumiService vertejumiServiss;
	private final IKursaDalibniekiRepo kursaDalibniekiRepo;
	private final IKursaDatumiRepo kursaDatumiRepo;
	private final IFilterService filterService;

	private static final String ATTR_PACKAGE = "package";
	private static final String VIEW_ERROR = "error-page";
	private static final String ATTR_VERTEJUMI = "vertejumi";
	private static final String REDIRECT_SHOW_ALL = "redirect:/vertejumi/CRUD/show/all";
	private static final String ATTR_MESSEGE = "message";
	private static final String VIEW_UPDATE = "vertejumi-update-page";
	private static final String VIEW_ADD = "vertejumi-add-page";

	private static final Logger logger = LoggerFactory.getLogger(VertejumiCRUDController.class);

	public VertejumiCRUDController(ICRUDVertejumiService vertejumiServiss, IKursaDalibniekiRepo kursaDalibniekiRepo,
			IKursaDatumiRepo kursaDatumiRepo, IFilterService filterService) {

		this.vertejumiServiss = vertejumiServiss;
		this.kursaDalibniekiRepo = kursaDalibniekiRepo;
		this.kursaDatumiRepo = kursaDatumiRepo;
		this.filterService = filterService;
	}

	@GetMapping("/show/all")
	public String getControllerShowAllVertejumi(Model model, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "3") int size, @RequestParam(required = false) Integer min,
			@RequestParam(required = false) Integer max) {
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

			model.addAttribute(ATTR_PACKAGE, visiVertejumi);
			return "vertejumi-all-page";
		} catch (Exception e) {
			model.addAttribute(ATTR_PACKAGE, e.getMessage());
			return VIEW_ERROR;
		}
	}

	@GetMapping("/show/all/{id}")
	public String getControllerShowVertejumsByID(@PathVariable(name = "id") Integer id, Model model) {
		try {
			Page<Vertejumi> vertejumi = vertejumiServiss.retrieveById(id);
			model.addAttribute(ATTR_PACKAGE, vertejumi);
			return "vertejumi-all-page";
		} catch (Exception e) {
			model.addAttribute(ATTR_PACKAGE, e.getMessage());
			return VIEW_ERROR;
		}
	}

	@GetMapping("/remove/{id}")
	public String getControllerRemoveVertejums(@PathVariable(name = "id") int id, Model model) {
		try {
			Vertejumi vertejumi = vertejumiServiss.retrieveById(id).getContent().getFirst();
			model.addAttribute(ATTR_VERTEJUMI, vertejumi);
			return "vertejumi-delete-confirm";
		} catch (Exception e) {
			model.addAttribute(ATTR_PACKAGE, e.getMessage());
			return VIEW_ERROR;
		}
	}

	@PostMapping("/remove/{id}")
	public String deleteConfirmed(@PathVariable(name = "id") int id) {
		try {
			vertejumiServiss.deleteById(id);
			return REDIRECT_SHOW_ALL;
		} catch (Exception e) {
			logger.error("Error while deleting vertejums id={}", id, e);
			return VIEW_ERROR;
		}
	}

	@GetMapping("/add")
	public String getControllerAddVertejums(Model model) {
		Vertejumi vertejumi = new Vertejumi();
		model.addAttribute(ATTR_VERTEJUMI, vertejumi);

		List<KursaDalibnieki> kursaDalibniekiList = new ArrayList<>();
		kursaDalibniekiRepo.findAll().forEach(kursaDalibniekiList::add);
		model.addAttribute("kursaDalibniekiList", kursaDalibniekiList);

		List<KursaDatumi> kursaDatumiList = new ArrayList<>();
		kursaDatumiRepo.findAll().forEach(kursaDatumiList::add);
		model.addAttribute("kursaDatumiList", kursaDatumiList);

		return VIEW_ADD;
	}

	@PostMapping("/add")
	public String postControllerAddVertejums(@ModelAttribute Vertejumi vertejumi, Model model) {
		if (vertejumi == null) {
			model.addAttribute(ATTR_PACKAGE, "The vertejumi is not given");
			return VIEW_ADD;
		}
		try {
			vertejumiServiss.create(vertejumi.getVertejums(), vertejumi.getDatums(), vertejumi.getKursaDalibnieki(),
					vertejumi.getKursaDatumi());
			return REDIRECT_SHOW_ALL;
		} catch (Exception e) {
			model.addAttribute(ATTR_MESSEGE, e.getMessage());
			logger.error("Error while creating vertejums: {}", vertejumi, e);
			return VIEW_ADD;
		}
	}

	@GetMapping("/update/{id}")
	public String getControllerUpdateVertejums(@PathVariable(name = "id") int id, Model model) {
		try {
			Vertejumi vertejumi = vertejumiServiss.retrieveById(id).getContent().getFirst();
			model.addAttribute(ATTR_VERTEJUMI, vertejumi);

			List<KursaDalibnieki> kursaDalibniekiList = new ArrayList<>();
			kursaDalibniekiRepo.findAll().forEach(kursaDalibniekiList::add);
			model.addAttribute("kursaDalibniekiList", kursaDalibniekiList);

			List<KursaDatumi> kursaDatumiList = new ArrayList<>();
			kursaDatumiRepo.findAll().forEach(kursaDatumiList::add);
			model.addAttribute("kursaDatumiList", kursaDatumiList);

		} catch (Exception e) {
			model.addAttribute(ATTR_MESSEGE, e.getMessage());

		}

		return VIEW_UPDATE;
	}

	@PostMapping("/update/{id}")
	public String postControllerUpdateVertejums(@PathVariable(name = "id") int id, Vertejumi vertejumi, Model model) {
		try {
			vertejumiServiss.updateById(id, vertejumi.getVertejums());
			return REDIRECT_SHOW_ALL;
		} catch (Exception e) {
			model.addAttribute(ATTR_MESSEGE, e.getMessage());
			logger.error("Error while updating vertejums id={}", id, e);
			return VIEW_UPDATE;
		}
	}
}
