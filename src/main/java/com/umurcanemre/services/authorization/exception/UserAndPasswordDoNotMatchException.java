package com.umurcanemre.services.authorization.exception;

public class UserAndPasswordDoNotMatchException extends RuntimeException {
	private static final long serialVersionUID = 7779433984556927095L;
	public UserAndPasswordDoNotMatchException() {
		super("Entered credentials do not match.");
	}
}
