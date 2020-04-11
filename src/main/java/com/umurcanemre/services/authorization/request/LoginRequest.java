package com.umurcanemre.services.authorization.request;

import lombok.Data;

@Data
public class LoginRequest {
	private String email;
	private char[] password;
}
