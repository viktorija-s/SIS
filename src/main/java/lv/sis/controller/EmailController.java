package lv.sis.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lv.sis.service.impl.EmailSendingServiceImpl;

@Controller
public class EmailController {
	@Autowired
	EmailSendingServiceImpl emailService = new EmailSendingServiceImpl();
	
	@GetMapping("/email")
	public String sendEmail(Model model) {
		
		emailService.sendSimpleMsg("vetaskovaelizaveta741@gmail.com", "s24vetajeli@venta.lv", "Test message", "Test message sent!", new File("src\\main\\resources\\fox.jpg"));
		model.addAttribute("package", "Email to s24vetajeli@venta.lv sent.");
		return "info";
	}
}
