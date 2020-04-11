package com.umurcanemre.services.authorization.eventlistener;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.umurcanemre.services.authorization.entity.User;
import com.umurcanemre.services.authorization.event.UserPasswordLostEvent;
import com.umurcanemre.services.authorization.factory.EmailFactory;
import com.umurcanemre.services.authorization.pojo.Email;
import com.umurcanemre.services.authorization.service.EmailService;

import lombok.Data;

@Component
@Data
public class UserPasswordLostEventListener implements ApplicationListener<UserPasswordLostEvent>  {
	private static final Logger logger = LoggerFactory.getLogger(NewUserEventListener.class);
	
	@Autowired
	private EmailService emailService;

	@Override
	public void onApplicationEvent(UserPasswordLostEvent event) {
		sendPasswordResetEmail(event.getUser());
	}
	private void sendPasswordResetEmail(User user) {
		Email email = EmailFactory.lostPasswordEmail(user);
		emailService.sendBasicMail(email);
		if (logger.isInfoEnabled())
			logger.info(MessageFormat.format("Password reset mail sent to : {0}.", email.getReceiver()));
		
	}

}
