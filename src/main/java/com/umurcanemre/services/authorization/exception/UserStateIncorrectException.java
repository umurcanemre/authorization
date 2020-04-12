package com.umurcanemre.services.authorization.exception;

import com.umurcanemre.services.authorization.entity.UserStatus;

public class UserStateIncorrectException extends RuntimeException{
	private static final long serialVersionUID = 5412972681732084183L;
	public UserStateIncorrectException(UserStatus status) {
		super("User status incorrect. Should be :"+status.toString());
	}

}
