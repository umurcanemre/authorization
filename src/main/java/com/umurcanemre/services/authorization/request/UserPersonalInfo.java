package com.umurcanemre.services.authorization.request;

import lombok.Data;

@Data
public class UserPersonalInfo {
	private String name;
	private String lastname;
	private String email;
}
