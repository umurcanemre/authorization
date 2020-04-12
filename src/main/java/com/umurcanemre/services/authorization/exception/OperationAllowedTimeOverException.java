package com.umurcanemre.services.authorization.exception;

public class OperationAllowedTimeOverException extends RuntimeException {
	private static final long serialVersionUID = 8842988169450263692L;
	public  OperationAllowedTimeOverException () {
		super("Allowed time for the operation has finished");
	}
}
