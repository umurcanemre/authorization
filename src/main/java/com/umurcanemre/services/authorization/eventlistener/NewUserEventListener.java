package com.umurcanemre.services.authorization.eventlistener;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.umurcanemre.services.authorization.entity.User;
import com.umurcanemre.services.authorization.event.NewUserEvent;
import com.umurcanemre.services.authorization.factory.EmailFactory;
import com.umurcanemre.services.authorization.pojo.Email;
import com.umurcanemre.services.authorization.service.EmailService;

import lombok.Data;

@Component
@Data
public class NewUserEventListener implements ApplicationListener<NewUserEvent> {
	private static final Logger logger = LoggerFactory.getLogger(NewUserEventListener.class);
	
	@Autowired
	private EmailService emailService;

	@Override
	public void onApplicationEvent(NewUserEvent event) {
		sendValidationEmail(event.getUser());
	}

	private void sendValidationEmail(User user) {
		Email email = EmailFactory.newUserEmail(user);
		emailService.sendBasicMail(email);
		if (logger.isInfoEnabled())
			logger.info(MessageFormat.format("User validation mail sent to : {0}.", email.getReceiver()));
	}

}
