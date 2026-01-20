package lv.sis.controller;

import java.io.File;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lv.sis.model.KursaDalibnieki;
import lv.sis.service.ICRUDKursaDalibniekiService;
import lv.sis.service.impl.EmailSendingServiceImpl;

@Controller
public class EmailController {
	
	private static final Logger logger = LoggerFactory.getLogger(EmailController.class);
	
	@Autowired
	EmailSendingServiceImpl emailService = new EmailSendingServiceImpl();
	
	@Autowired
	ICRUDKursaDalibniekiService dalService;
	
	@GetMapping("/email")
	public String sendEmail(Model model) {
		logger.info("Request received to send emails");
		
		ArrayList<String> emails = new ArrayList<String>();
		
		try {
			for (KursaDalibnieki dal: dalService.retrieveAll()) {
				emails.add(dal.getEpasts());
			}
			
			for (String email : emails) {
				emailService.sendSimpleMsg("vetaskovaelizaveta741@gmail.com", email, "Test message", "Test message sent", new File("src\\main\\resources\\fox.jpg"));
				logger.debug("Sent email to {}", email);
			}
			
			logger.info("All emails sent successfully");
			model.addAttribute("package", "Emails sent.");
			return "info";
		} catch (Exception e) {
			logger.error("Error while sending emails: {}", e.getMessage(), e);
			model.addAttribute("package", e.getMessage());
			return "error-page";
		}
		
	}
}
