package lv.sis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lv.sis.service.IPDFCreatorService;

@Controller
public class PDFCreatorTestController {
	
	@Autowired
	private IPDFCreatorService pdfService;
	
	@GetMapping("/pdf/{kdid}/{kid}") // localhost:8080/pdf/1/2
	public String getPDFController(Model model, @PathVariable(name = "kdid") int kdid, @PathVariable(name = "kid") int kid) {
		try {
			pdfService.createCertificateAsPDF(kdid, kid);
			model.addAttribute("package", "PDF ir izveidots");
			return "data-page";
		} catch (Exception e) {
            e.printStackTrace();
			model.addAttribute("message", e.getMessage());
			return "data-page";
		}
	}

}
