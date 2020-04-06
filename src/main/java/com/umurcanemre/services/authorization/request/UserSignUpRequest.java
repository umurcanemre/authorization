package com.umurcanemre.services.authorization.request;

import lombok.Data;

@Data
public class UserSignUpRequest {
	private String name;
	private String lastname;
	private String email;
}
