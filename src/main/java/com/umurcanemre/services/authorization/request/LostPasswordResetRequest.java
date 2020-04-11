package com.umurcanemre.services.authorization.request;

import lombok.Data;

@Data
public class LostPasswordResetRequest {
	private String email;
	private String passwordResetCode;
	private char[] newPassword;
}
