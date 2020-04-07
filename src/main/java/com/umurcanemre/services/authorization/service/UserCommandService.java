package com.umurcanemre.services.authorization.service;

import com.umurcanemre.services.authorization.request.UserDeleteRequest;
import com.umurcanemre.services.authorization.request.UserPasswordResetRequest;
import com.umurcanemre.services.authorization.request.UserSignUpRequest;
import com.umurcanemre.services.authorization.request.UserUpdateRequest;

public interface UserCommandService {
	void createUser(UserSignUpRequest request);
	void updateUser(UserUpdateRequest request);
	void deleteUser(UserDeleteRequest request);
	void validateUser(String email, String validationCode);
	void resetPassword(UserPasswordResetRequest request);
}
