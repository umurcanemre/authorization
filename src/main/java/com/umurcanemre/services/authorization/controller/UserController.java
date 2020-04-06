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
import com.umurcanemre.services.authorization.request.UserSignUpRequest;
import com.umurcanemre.services.authorization.request.ValidationRequest;
import com.umurcanemre.services.authorization.service.UserCommandService;
import com.umurcanemre.services.authorization.service.UserQueryService;

import lombok.Data;

//TODO: 
// persist userstatus
// purge validationCode entity
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
	public void updateUser(@RequestBody UserSignUpRequest request) {
		commandService.updateUser(request);
	}
	
	@GetMapping("{id}")
	public User getUser(@PathVariable String id) {
		return queryService.getUser(id);
	}

	@PostMapping("/validate")
	public void validate(@RequestBody ValidationRequest request) {
		commandService.validateUser(request);
	}
}
