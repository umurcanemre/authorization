package com.umurcanemre.services.authorization.serviceimpl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.umurcanemre.services.authorization.entity.User;
import com.umurcanemre.services.authorization.entity.UserStatus;
import com.umurcanemre.services.authorization.event.NewUserEvent;
import com.umurcanemre.services.authorization.event.UserPasswordLostEvent;
import com.umurcanemre.services.authorization.repository.UserRepository;
import com.umurcanemre.services.authorization.request.LostPasswordResetRequest;
import com.umurcanemre.services.authorization.request.UserDeleteRequest;
import com.umurcanemre.services.authorization.request.UserPasswordLostRequest;
import com.umurcanemre.services.authorization.request.UserPasswordResetRequest;
import com.umurcanemre.services.authorization.request.UserSignUpRequest;
import com.umurcanemre.services.authorization.request.UserUpdateRequest;
import com.umurcanemre.services.authorization.service.UserCommandService;

import lombok.Data;

@Service
@Data
public class UserCommandServiceImpl implements UserCommandService, ApplicationEventPublisherAware {
	@Autowired
	private UserRepository repository;
	@Autowired
	private Environment env;
    private ApplicationEventPublisher applicationEventPublisher;

	@Override
	public void createUser(UserSignUpRequest request) {
		User user = new User(request,
				hashPassword(request.getPassword(), Integer.parseInt(env.getProperty("bcrypt.iteration"))));
		repository.save(user);
		applicationEventPublisher.publishEvent(new NewUserEvent(user));
	}

	@Override
	public void updateUser(UserUpdateRequest request) {
		User user = repository.findById(request.getId()).orElse(null);
		if (userValidityCheck(user) && user.getStatus().equals(UserStatus.ACTIVE)) {
			user.update(request.getPersonalInfo());
			repository.save(user);
		}
	}

	@Override
	public void deleteUser(UserDeleteRequest request) {
		User user = repository.findById(request.getId()).orElse(null);
		if (user != null) {
			repository.deleteById(request.getId());
		}
	}

	@Override
	public void validateUser(String id, String validationCode) {
		User user = repository.findByIdAndValidationCode(id, validationCode).orElse(null);
		if (user != null && validationWithinAllowedTime(user)) {
			user.validate();
			repository.save(user);
		}
	}

	private boolean validationWithinAllowedTime(User user) {
		long allowedTime = Long.parseLong(env.getProperty("validation.allowedtime.duration"));
		ChronoUnit cu = ChronoUnit.valueOf(env.getProperty("validation.allowedtime.unit"));
		return cu.between(user.getJoinTimestamp(), LocalDateTime.now()) <= allowedTime;
	}

	@Override
	public void resetPassword(UserPasswordResetRequest request) {
		User user = repository.findById(request.getId()).orElse(null);

		if (userValidityCheck(user) && BCrypt.checkpw(new String(request.getOldPassword()), user.getPassword())) {
			user.setPassword(
					hashPassword(request.getNewPassword(), Integer.parseInt(env.getProperty("bcrypt.iteration"))));
			repository.save(user);
		}
	}

	@Override
	public void resetLostPassword(LostPasswordResetRequest request) {
		User user = repository.findByEmailAndPwResetCode(request.getEmail(), request.getPasswordResetCode()).orElse(null);

		if (userValidityCheck(user) && pwResetWithinAllowedTime(user)) {
			user.setPassword(
					hashPassword(request.getNewPassword(), Integer.parseInt(env.getProperty("bcrypt.iteration"))));
			user.setPwResetCode(null);
			user.setPwResetCodeTimestamp(null);
			repository.save(user);
		}
		
	}

	private boolean pwResetWithinAllowedTime(User user) {
		long allowedTime = Long.parseLong(env.getProperty("pwreset.allowedtime.duration"));
		ChronoUnit cu = ChronoUnit.valueOf(env.getProperty("pwreset.allowedtime.unit"));
		return cu.between(user.getPwResetCodeTimestamp(), LocalDateTime.now()) <= allowedTime;
	}

	private String hashPassword(char[] pw, int iteration) {
		return BCrypt.hashpw(new String(pw), BCrypt.gensalt(iteration));
	}

	@Override
	public void createPasswordLostCode(UserPasswordLostRequest request) {
		User user = repository.findByEmail(request.getEmail()).orElse(null);
		if(userValidityCheck(user)) {
			user.createPWResetCode();
			repository.save(user);
			applicationEventPublisher.publishEvent(new UserPasswordLostEvent(user));
		}		
	}
	
	private boolean userValidityCheck(User user) {
		return user != null && user.getStatus().equals(UserStatus.ACTIVE); 
	}
}
