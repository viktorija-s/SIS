package lv.sis.service.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSendingServiceImpl {
	
	private static final Logger logger = LoggerFactory.getLogger(EmailSendingServiceImpl.class);
	
	@Autowired
	JavaMailSender mailSender;
	
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
			logger.info("Email sent from '{}' to '{}' with subject '{}'", from, to, subject);
		} catch (Exception e) {
			logger.error("Failed to send email from '{}' to '{}': {}", from, to, e.getMessage(), e);
			System.out.println(e.getMessage());
		}
	}
}
