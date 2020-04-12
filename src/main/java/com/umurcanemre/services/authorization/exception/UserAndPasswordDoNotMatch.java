package com.umurcanemre.services.authorization.exception;

public class UserAndPasswordDoNotMatch extends RuntimeException {
	private static final long serialVersionUID = 7779433984556927095L;
	public UserAndPasswordDoNotMatch() {
		super("Entered credentials do not match.");
	}
}
