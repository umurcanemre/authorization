package com.umurcanemre.services.authorization.exception;

public class UserNotLoggedInException extends RuntimeException {
	private static final long serialVersionUID = -6785202315946729180L;
	public UserNotLoggedInException() {
		super("This operation requires user to login");
	}
}
