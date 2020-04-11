package com.umurcanemre.services.authorization.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umurcanemre.services.authorization.controller.UserController;
import com.umurcanemre.services.authorization.entity.User;
import com.umurcanemre.services.authorization.pojo.Email;
import com.umurcanemre.services.authorization.request.LostPasswordResetRequest;

public final class EmailFactory {
	private static Environment env;

	private static final String greetingTextHeader = "Welcome to auth app\nPlease proceed to following link to activate your account\n";
	private static final String greetingEmailSubject = "Authorization service validation";
	private static final String passwordResetEmailSubject = "Authorization service password reset";
	private static final String NEWPW = "your-new-password" ;
	private static final ObjectMapper objectMapper = new ObjectMapper();
	private static final Logger logger = LoggerFactory.getLogger(EmailFactory.class); 
	private EmailFactory() {};
	
	public static void setEnv(Environment env) {
		if(env != null) {
			EmailFactory.env = env;
		}
	}
	
	public static Email newUserEmail(final User u) {
		StringBuilder body = new StringBuilder();
		body.append(greetingTextHeader).append(getValidationLink(u));
		
		return new Email(u.getEmail(), greetingEmailSubject, body.toString());
	}
	private static String getValidationLink(final User u) {
		String validationUrl =  env.getProperty("application.domain")+UserController.getUserValidationUrl() ;
		validationUrl = validationUrl.replace(UserController.getId(), u.getId());
		validationUrl = validationUrl.replace(UserController.getValidation(), u.getValidationCode());
		return validationUrl;
	}
	
	public static Email lostPasswordEmail(final User u) {
		return new Email(u.getEmail(), passwordResetEmailSubject, lostPWEmailBody(u));
	}
	private static String lostPWEmailBody(final User u) {
		StringBuilder body = new StringBuilder();
		body.append("Please proceed to ")
		.append(getPWResetLink())
		.append(" and POST the following request body\n");
		
		LostPasswordResetRequest request = new LostPasswordResetRequest();
		request.setEmail(u.getEmail());
		request.setPasswordResetCode(u.getPwResetCode());
		request.setNewPassword(NEWPW.toCharArray());
		String requestJson;
		try {
			requestJson = objectMapper.writeValueAsString(request);
		} catch (JsonProcessingException e) {
			logger.error(e.toString());
			e.printStackTrace();
			requestJson = "{}";
		}
		return body.append(requestJson).toString() ;
	}
	private static final String getPWResetLink() {
		return env.getProperty("application.domain")+UserController.getPasswordResetURL() ;
	}
}
