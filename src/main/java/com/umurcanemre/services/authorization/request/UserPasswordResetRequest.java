package com.umurcanemre.services.authorization.request;

import lombok.Data;

@Data
public class UserPasswordResetRequest {
	private String id;
	private char[] oldPassword;
	private char[] newPassword;
}
