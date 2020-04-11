package com.umurcanemre.services.authorization.request;

import lombok.Data;

@Data
public class UserPasswordLostRequest {
	private String email; 
}
