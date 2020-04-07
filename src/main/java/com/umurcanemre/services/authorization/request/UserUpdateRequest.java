package com.umurcanemre.services.authorization.request;

import lombok.Data;

@Data
public class UserUpdateRequest {
	private UserPersonalInfo personalInfo;
	private String id;
}
