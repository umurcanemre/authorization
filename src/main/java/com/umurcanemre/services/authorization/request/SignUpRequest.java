package com.umurcanemre.services.authorization.request;

import lombok.Data;

@Data
public class SignUpRequest {
	private String name;
	private String lastname;
	private String email;
}
