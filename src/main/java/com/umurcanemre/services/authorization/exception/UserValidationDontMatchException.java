package com.umurcanemre.services.authorization.exception;

public class UserValidationDontMatchException extends RuntimeException {
	private static final long serialVersionUID = 3864362221846635420L;

	public UserValidationDontMatchException () {
		super("Given email and validation code doesn't match records");
	}
}
