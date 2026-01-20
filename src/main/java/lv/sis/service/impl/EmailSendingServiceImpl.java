package lv.sis.service.impl;

import java.io.File;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSendingServiceImpl {
	
	private final JavaMailSender mailSender;
	
	public EmailSendingServiceImpl(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	public void sendSimpleMsg(String from, String to, String subject, String text, File attachment) {

		MimeMessage msg = mailSender.createMimeMessage();
		
		try {
			MimeMessageHelper helper = new MimeMessageHelper(msg, true);
			helper.setFrom(from);
			
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(text);
			
			FileSystemResource file = new FileSystemResource(attachment);
			helper.addAttachment(file.getFilename(), file);
			
			mailSender.send(msg);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
