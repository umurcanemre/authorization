package com.umurcanemre.services.authorization.serviceimpl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.umurcanemre.services.authorization.entity.User;
import com.umurcanemre.services.authorization.entity.UserStatus;
import com.umurcanemre.services.authorization.repository.UserRepository;
import com.umurcanemre.services.authorization.request.UserDeleteRequest;
import com.umurcanemre.services.authorization.request.UserPasswordResetRequest;
import com.umurcanemre.services.authorization.request.UserSignUpRequest;
import com.umurcanemre.services.authorization.request.UserUpdateRequest;
import com.umurcanemre.services.authorization.service.UserCommandService;

import lombok.Data;

@Service
@Data
public class UserCommandServiceImpl implements UserCommandService {
	@Autowired
	private UserRepository repository;
	@Autowired
	private Environment env;

	@Override
	public void createUser(UserSignUpRequest request) {
		User user = new User(request,
				hashPassword(request.getPassword(), Integer.parseInt(env.getProperty("bcrypt.iteration"))));
		repository.save(user);
	}

	@Override
	public void updateUser(UserUpdateRequest request) {
		User user = repository.findById(request.getId()).orElse(null);
		if (user != null && user.getStatus().equals(UserStatus.ACTIVE)) {
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
	public void validateUser(String email, String validationCode) {
		User user = repository.findByEmailAndValidationCode(email, validationCode).orElse(null);
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

		if (user != null && BCrypt.checkpw(new String(request.getOldPassword()), user.getPassword())) {
			user.setPassword(
					hashPassword(request.getOldPassword(), Integer.parseInt(env.getProperty("bcrypt.iteration"))));
			repository.save(user);
		}
	}

	private String hashPassword(char[] pw, int iteration) {
		return BCrypt.hashpw(new String(pw), BCrypt.gensalt(iteration));
	}
}
