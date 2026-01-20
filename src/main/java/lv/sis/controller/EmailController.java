package lv.sis.controller;

import java.io.File;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lv.sis.model.KursaDalibnieki;
import lv.sis.service.ICRUDKursaDalibniekiService;
import lv.sis.service.impl.EmailSendingServiceImpl;

@Controller
public class EmailController {
	
	private final EmailSendingServiceImpl emailService;
	private final ICRUDKursaDalibniekiService dalService;
	
	public EmailController(EmailSendingServiceImpl emailService, ICRUDKursaDalibniekiService dalService) {
		this.emailService = emailService;
		this.dalService = dalService;
	}
	
	@GetMapping("/email")
	public String sendEmail(Model model) {
		
		ArrayList<String> emails = new ArrayList<>();
		
		try {
			for (KursaDalibnieki dal: dalService.retrieveAll()) {
				emails.add(dal.getEpasts());
			}
			
			for (String email : emails) {
				emailService.sendSimpleMsg("vetaskovaelizaveta741@gmail.com", email, "Test message", "Test message sent", new File("src\\main\\resources\\fox.jpg"));
			}
			
			model.addAttribute("package", "Emails sent.");
			return "info";
		} catch (Exception e) {
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
		
	}
}
