package com.umurcanemre.services.authorization.request;

import com.umurcanemre.services.authorization.entity.UserStatus;

import lombok.Data;

@Data
public class UserSystemStateInfo {
	private String validationCode;
	private UserStatus status;
}
