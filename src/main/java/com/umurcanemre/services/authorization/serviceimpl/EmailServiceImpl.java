package com.umurcanemre.services.authorization.serviceimpl;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.umurcanemre.services.authorization.pojo.Email;
import com.umurcanemre.services.authorization.service.EmailService;

import lombok.Data;

@Service
@Data
public class EmailServiceImpl implements EmailService {
	@Autowired
	private JavaMailSender mailSender;
	private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
	
	@Override
	public void sendBasicMail(Email email) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(email.getReceiver());
		mailMessage.setSubject(email.getSubject());
		mailMessage.setText(email.getBody());
		try {
		mailSender.send(mailMessage);
		}
		catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();

			if (logger.isInfoEnabled())
				logger.info(MessageFormat.format("failed mail content : {0}.", email.getReceiver()));
		}
	}
}
