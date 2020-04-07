package com.umurcanemre.services.authorization.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.umurcanemre.services.authorization.entity.User;
import com.umurcanemre.services.authorization.request.UserPasswordResetRequest;
import com.umurcanemre.services.authorization.request.UserSignUpRequest;
import com.umurcanemre.services.authorization.request.UserUpdateRequest;
import com.umurcanemre.services.authorization.service.UserCommandService;
import com.umurcanemre.services.authorization.service.UserQueryService;

import lombok.Data;

//TODO: 
// persist userstatus
// create Mail service
// valid annotation


@Data
@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserQueryService queryService;
	@Autowired
	private UserCommandService commandService; 

	@PostMapping("/signup")
	public void signup(@RequestBody UserSignUpRequest request) {
		commandService.createUser(request);
	}
	
	@PutMapping("/update")
	public void updateUser(@RequestBody UserUpdateRequest request) {
		commandService.updateUser(request);
	}
	
	@GetMapping("{id}")
	public User getUser(@PathVariable String id) {
		return queryService.getUser(id);
	}

	@PostMapping("/validate/{email}/{validationcode}")
	public void validate(@PathVariable String email, @PathVariable String validationcode) {
		commandService.validateUser(email,validationcode);
	}
	
	@PostMapping("/passwordreset")
	public void resetPassword(@RequestBody UserPasswordResetRequest request) {
		commandService.resetPassword(request);
	}
}
