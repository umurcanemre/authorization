package com.umurcanemre.services.authorization.request;

import lombok.Data;

@Data
public class UserSignUpRequest {
	private UserPersonalInfo personalInfo;
	private char[] password;
}
