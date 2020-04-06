package com.umurcanemre.services.authorization.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.umurcanemre.services.authorization.entity.User;
import com.umurcanemre.services.authorization.entity.ValidationCode;
import com.umurcanemre.services.authorization.repository.UserRepository;
import com.umurcanemre.services.authorization.repository.ValidationCodeRepository;
import com.umurcanemre.services.authorization.request.UserDeleteRequest;
import com.umurcanemre.services.authorization.request.UserSignUpRequest;
import com.umurcanemre.services.authorization.request.ValidationRequest;
import com.umurcanemre.services.authorization.service.UserCommandService;

import lombok.Data;

@Service
@Data
public class UserCommandServiceImpl implements UserCommandService {
	@Autowired
	private UserRepository repository; 
	@Autowired
	private ValidationCodeRepository validationCodeRepository; 
	
	@Override
	public void createUser(UserSignUpRequest request) {
		User user = new User(request);
		repository.save(user);
	}

	@Override
	public void updateUser(UserSignUpRequest request) {
		Optional<User> query = repository.findByEmail(request.getEmail());
		if(query.isPresent()) {
			User user = query.get();
			user.update(request);			
			repository.save(user);
		}
	}

	@Override
	public void deleteUser(UserDeleteRequest request) {
		User user = repository.findByIdAndEmail(request.getId(), request.getEmail()).orElse(null);
		if(user != null) {
			repository.deleteById(request.getId());
		}
	}

	@Override
	public void validateUser(ValidationRequest request) {
		User user = repository.findByEmail(request.getEmail()).orElse(null);
		if(user != null && user.getValidationCode() != null) {
			ValidationCode validationCode = user.getValidationCode();
			if(validationCode.getCode().contentEquals(request.getValidationCode())) {
				user.validate();
				//validationCodeRepository.delete(validationCode);
				repository.save(user);
			}
		}
	}

}
