package com.umurcanemre.services.authorization.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.umurcanemre.services.authorization.request.LoginRequest;
import com.umurcanemre.services.authorization.request.LostPasswordResetRequest;
import com.umurcanemre.services.authorization.request.UserEmailRequest;
import com.umurcanemre.services.authorization.request.UserPasswordResetRequest;
import com.umurcanemre.services.authorization.request.UserSignUpRequest;
import com.umurcanemre.services.authorization.request.UserUpdateRequest;
import com.umurcanemre.services.authorization.response.UserResponse;
import com.umurcanemre.services.authorization.service.UserCommandService;
import com.umurcanemre.services.authorization.service.UserQueryService;

import lombok.Data;

@Data
@RestController
@RequestMapping("/user")
public class UserController {
	private static final String URLROOT = "/user";
	private static final String ID = "{id}";
	private static final String VALIDATIONCODE = "{validationcode}";
	private static final String VALIDATE_URL = "/validate/"+ID+"/"+VALIDATIONCODE;
	private static final String RESETLOSTPASSWORD_URL = "/resetlostpw";

	public static String getUserValidationUrl() {
		return URLROOT + VALIDATE_URL;
	}
	public static String getId() {
		return ID;
	}
	public static String getValidation() {
		return VALIDATIONCODE;
	}
	public static String getPasswordResetURL() {
		return RESETLOSTPASSWORD_URL;
	}

	@Autowired
	private UserQueryService queryService;
	@Autowired
	private UserCommandService commandService;

	//Create new user
	@PostMapping("/signup")
	public UserResponse signup(@RequestBody UserSignUpRequest request) {
		return commandService.createUser(request);
	}

	//Update allowed fields of user 
	@PutMapping("/update")
	public UserResponse updateUser(@RequestBody UserUpdateRequest request) {
		return commandService.updateUser(request);
	}

	//get user by id
	@GetMapping("{id}")
	public UserResponse getUser(@PathVariable String id) {
		return queryService.getUser(id);
	}

	//Validate user
	@GetMapping(VALIDATE_URL)
	public void validate(@PathVariable String id, @PathVariable String validationcode) {
		commandService.validateUser(id, validationcode);
	}

	//Password reset link for users that know their old password 
	@PostMapping("/passwordreset")
	public void resetPassword(@RequestBody UserPasswordResetRequest request) {
		commandService.resetPassword(request);
	}

	//Creates password lost request, a mail is sent to account owner with reset instructions
	@PostMapping("/passwordlost")
	public void lostPassword(@RequestBody UserEmailRequest request) {
		commandService.createPasswordLostCode(request);
	}

	//Reseting lost password endpoint. Uses a code that allows the user to reset it's password without entering the old one 
	@PostMapping(RESETLOSTPASSWORD_URL)
	public void resetLostPassword(@RequestBody LostPasswordResetRequest request) {
		commandService.resetLostPassword(request);
	}

	@PostMapping("/login")
	public void login(@RequestBody LoginRequest request) {
		commandService.login(request);
	}

	@PostMapping("/logout")
	public void logout(@RequestBody UserEmailRequest request) {
		commandService.logout(request);
	}
	
	//Returns count of sessions that are considered to be live
	@GetMapping("/onlinecount")
	public Integer getOnlineUserCount() {
		return queryService.getActiveSessionsCount();
	}
	
	//Returns average duration of validation of users in minutes
	@GetMapping("/avgvalidateduration")
	public Double getOAvgValidateDuration() {
		return queryService.getAverageValidationDuration();
	}
	
	//Returns new users created between given dates
	@GetMapping("/newusercountbetween/{startdate}/{enddate}")
	public Integer getNewUserCountBetweenDates(@PathVariable String startdate,@PathVariable String enddate) {
		return queryService.getNewUserCountWithinDates(LocalDate.parse(startdate), LocalDate.parse(enddate));
	}
	
	
	//Returns failed validation count to the date 
	@GetMapping("/failedvalidationcount")
	public Integer getFailedValidationCount() {
		return queryService.getFailedValidationCount();
	}
}
