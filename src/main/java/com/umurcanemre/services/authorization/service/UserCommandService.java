package com.umurcanemre.services.authorization.service;

import com.umurcanemre.services.authorization.request.UserDeleteRequest;
import com.umurcanemre.services.authorization.request.UserSignUpRequest;
import com.umurcanemre.services.authorization.request.ValidationRequest;

public interface UserCommandService {
	void createUser(UserSignUpRequest request);
	void updateUser(UserSignUpRequest request);
	void deleteUser(UserDeleteRequest request);
	void validateUser(ValidationRequest request);
}
