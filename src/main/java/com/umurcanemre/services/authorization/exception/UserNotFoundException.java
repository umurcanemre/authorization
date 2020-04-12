package com.umurcanemre.services.authorization.exception;

public class UserNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 3778436864910353579L;

	public UserNotFoundException (boolean isId, String value){
		super("User with "+ (isId ? "id" : "email")+"  : "+value+" not found");
	}
}
