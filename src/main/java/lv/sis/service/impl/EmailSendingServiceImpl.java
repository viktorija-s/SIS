package lv.sis.service.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSendingServiceImpl {
	
	@Autowired
	JavaMailSender mailSender;
	
	public void sendSimpleMsg(String from, String to, String subject, String text, File attachment) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom(from);
		msg.setTo(to);
		msg.setSubject(subject);
		msg.setText(text);
		
		mailSender.send(msg);
	}
}
