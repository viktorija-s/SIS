package lv.sis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lv.sis.service.IPDFCreatorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class PDFCreatorTestController {
	
	private static final Logger logger = LoggerFactory.getLogger(PDFCreatorTestController.class);
	
	@Autowired
	private IPDFCreatorService pdfService;
	
	@GetMapping("/pdf/{kdid}/{kid}") // localhost:8080/pdf/1/2
	public String getPDFController(Model model, @PathVariable(name = "kdid") int kdid, @PathVariable(name = "kid") int kid) {
		logger.info("Generating PDF certificate for participant ID {} and course ID {}", kdid, kid);
		try {
			pdfService.createCertificateAsPDF(kdid, kid);
			logger.info("Successfully generated PDF certificate for participant ID {} and course ID {}", kdid, kid);
			model.addAttribute("package", "PDF ir izveidots");
			return "data-page";
		} catch (Exception e) {
			logger.error("Failed to create PDF for kdid={}, kid={}: {}", kdid, kid, e.getMessage(), e);
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
	}

}
