package com.umurcanemre.services.authorization.request;

import lombok.Data;

@Data
public class UserDeleteRequest {
	private String email;
	private String id;
}
