package com.umurcanemre.services.authorization.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.umurcanemre.services.authorization.exception.OperationAllowedTimeOverException;
import com.umurcanemre.services.authorization.exception.UserAndPasswordDoNotMatchException;
import com.umurcanemre.services.authorization.exception.UserNotFoundException;
import com.umurcanemre.services.authorization.exception.UserNotLoggedInException;
import com.umurcanemre.services.authorization.exception.UserStateIncorrectException;
import com.umurcanemre.services.authorization.exception.UserValidationDontMatchException;

@ControllerAdvice
public class UserAdvice {
	@ResponseBody
	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String userNotFoundException(UserNotFoundException ex) {
		return ex.getMessage();
	}
	@ResponseBody
	@ExceptionHandler(UserNotLoggedInException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	String userNotLoggedIn(UserNotLoggedInException ex) {
		return ex.getMessage();
	}
	@ResponseBody
	@ExceptionHandler(UserAndPasswordDoNotMatchException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	String userAndPasswordDoNotMatch(UserAndPasswordDoNotMatchException ex) {
		return ex.getMessage();
	}
	@ResponseBody
	@ExceptionHandler(UserStateIncorrectException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	String userStateIncorrectException(UserStateIncorrectException ex) {
		return ex.getMessage();
	}
	@ResponseBody
	@ExceptionHandler(UserValidationDontMatchException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	String userValidationDontMatchException(UserValidationDontMatchException ex) {
		return ex.getMessage();
	}
	@ResponseBody
	@ExceptionHandler(OperationAllowedTimeOverException.class)
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	String operationAllowedTimeOver(OperationAllowedTimeOverException ex) {
		return ex.getMessage();
	}
}
