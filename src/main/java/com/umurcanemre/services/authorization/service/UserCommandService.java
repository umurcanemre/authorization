package com.umurcanemre.services.authorization.service;

import com.umurcanemre.services.authorization.request.LoginRequest;
import com.umurcanemre.services.authorization.request.LostPasswordResetRequest;
import com.umurcanemre.services.authorization.request.UserDeleteRequest;
import com.umurcanemre.services.authorization.request.UserEmailRequest;
import com.umurcanemre.services.authorization.request.UserPasswordResetRequest;
import com.umurcanemre.services.authorization.request.UserSignUpRequest;
import com.umurcanemre.services.authorization.request.UserUpdateRequest;
import com.umurcanemre.services.authorization.response.UserResponse;

public interface UserCommandService {
	UserResponse createUser(UserSignUpRequest request);
	UserResponse updateUser(UserUpdateRequest request);
	void deleteUser(UserDeleteRequest request);
	void validateUser(String email, String validationCode);
	void resetPassword(UserPasswordResetRequest request);
	void resetLostPassword(LostPasswordResetRequest request);
	void createPasswordLostCode(UserEmailRequest request);
	void login(LoginRequest request);
	void logout(UserEmailRequest request);
}
