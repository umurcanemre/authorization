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
import com.umurcanemre.services.authorization.exception.OperationAllowedTimeOverException;
import com.umurcanemre.services.authorization.exception.UserAndPasswordDoNotMatchException;
import com.umurcanemre.services.authorization.exception.UserNotFoundException;
import com.umurcanemre.services.authorization.exception.UserNotLoggedInException;
import com.umurcanemre.services.authorization.exception.UserStateIncorrectException;
import com.umurcanemre.services.authorization.exception.UserValidationDontMatchException;
import com.umurcanemre.services.authorization.repository.UserRepository;
import com.umurcanemre.services.authorization.request.LoginRequest;
import com.umurcanemre.services.authorization.request.LostPasswordResetRequest;
import com.umurcanemre.services.authorization.request.UserDeleteRequest;
import com.umurcanemre.services.authorization.request.UserEmailRequest;
import com.umurcanemre.services.authorization.request.UserPasswordResetRequest;
import com.umurcanemre.services.authorization.request.UserSignUpRequest;
import com.umurcanemre.services.authorization.request.UserUpdateRequest;
import com.umurcanemre.services.authorization.response.UserResponse;
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
	public UserResponse createUser(UserSignUpRequest request) {
		User user = new User(request,
				hashPassword(request.getPassword(), Integer.parseInt(env.getProperty("bcrypt.iteration"))));
		user = repository.save(user);
		applicationEventPublisher.publishEvent(new NewUserEvent(user));
		return new UserResponse(user);
	}

	@Override
	public UserResponse updateUser(UserUpdateRequest request) {
		User user = repository.findById(request.getId())
				.orElseThrow(() -> new UserNotFoundException(true, request.getId()));
		userValidtyAndSessionCheck(user);
		user.update(request.getPersonalInfo());
		repository.save(user);
		return new UserResponse(user);
	}

	@Override
	public void deleteUser(UserDeleteRequest request) {
		User user = repository.findById(request.getId())
				.orElseThrow(() -> new UserNotFoundException(true, request.getId()));
		userValidtyAndSessionCheck(user);
		repository.deleteById(request.getId());
	}

	@Override
	public void validateUser(String id, String validationCode) {
		User user = repository.findByIdAndValidationCode(id, validationCode)
				.orElseThrow(() -> new UserValidationDontMatchException());
		validationWithinAllowedTime(user);
		user.validate();
		repository.save(user);
	}

	private void validationWithinAllowedTime(User user) {
		long allowedTime = Long.parseLong(env.getProperty("validation.allowedtime.duration"));
		ChronoUnit cu = ChronoUnit.valueOf(env.getProperty("validation.allowedtime.unit"));
		if (cu.between(user.getJoinTimestamp(), LocalDateTime.now()) > allowedTime)
			throw new OperationAllowedTimeOverException();
	}

	@Override
	public void resetPassword(UserPasswordResetRequest request) {
		User user = repository.findById(request.getId())
				.orElseThrow(() -> new UserNotFoundException(true, request.getId()));

		userValidtyAndSessionCheck(user);
		if (!BCrypt.checkpw(new String(request.getOldPassword()), user.getPassword())) {
			throw new UserAndPasswordDoNotMatchException();
		}
		user.setPassword(hashPassword(request.getNewPassword(), Integer.parseInt(env.getProperty("bcrypt.iteration"))));
		repository.save(user);
	}

	@Override
	public void resetLostPassword(LostPasswordResetRequest request) {
		User user = repository.findByEmailAndPwResetCode(request.getEmail(), request.getPasswordResetCode())
				.orElseThrow(() -> new UserValidationDontMatchException());

		userValidityCheck(user);
		checkPwResetWithinAllowedTime(user);
		user.setPassword(hashPassword(request.getNewPassword(), Integer.parseInt(env.getProperty("bcrypt.iteration"))));
		user.setPwResetCode(null);
		user.setPwResetCodeTimestamp(null);
		repository.save(user);

	}

	private void checkPwResetWithinAllowedTime(User user) {
		long allowedTime = Long.parseLong(env.getProperty("pwreset.allowedtime.duration"));
		ChronoUnit cu = ChronoUnit.valueOf(env.getProperty("pwreset.allowedtime.unit"));
		if (cu.between(user.getPwResetCodeTimestamp(), LocalDateTime.now()) > allowedTime) {
			throw new OperationAllowedTimeOverException();
		}
	}

	private void userSessionLive(User user) {
		if (!user.isSessionLive()) {
			throw new UserNotLoggedInException();
		}
		long allowedTime = Long.parseLong(env.getProperty("user.livesession.allowedtime.duration"));
		ChronoUnit cu = ChronoUnit.valueOf(env.getProperty("user.livesession.allowedtime.unit"));
		if (cu.between(user.getLastLoginTimestamp(), LocalDateTime.now()) > allowedTime) {
			throw new UserNotLoggedInException();
		}
	}

	private String hashPassword(char[] pw, int iteration) {
		return BCrypt.hashpw(new String(pw), BCrypt.gensalt(iteration));
	}

	@Override
	public void createPasswordLostCode(UserEmailRequest request) {
		User user = repository.findByEmail(request.getEmail())
				.orElseThrow(() -> new UserNotFoundException(false, request.getEmail()));
		userValidityCheck(user);
		user.createPWResetCode();
		repository.save(user);
		applicationEventPublisher.publishEvent(new UserPasswordLostEvent(user));

	}

	private void userValidityCheck(User user) {
		if (!user.getStatus().equals(UserStatus.ACTIVE))
			throw new UserStateIncorrectException(UserStatus.ACTIVE);
	}

	private void userValidtyAndSessionCheck(User user) {
		userValidityCheck(user);
		userSessionLive(user);
	}

	@Override
	public void login(LoginRequest request) {
		User user = repository.findByEmail(request.getEmail())
				.orElseThrow(() -> new UserNotFoundException(false, request.getEmail()));
		userValidityCheck(user);
		if (!BCrypt.checkpw(new String(request.getPassword()), user.getPassword())) {
			throw new UserAndPasswordDoNotMatchException();
		}
		user.login();
		repository.save(user);

	}

	@Override
	public void logout(UserEmailRequest request) {
		User user = repository.findByEmail(request.getEmail())
				.orElseThrow(() -> new UserNotFoundException(false, request.getEmail()));
		userValidtyAndSessionCheck(user);
		user.logout();
		repository.save(user);
	}
}
