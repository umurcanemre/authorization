package com.umurcanemre.services.authorization.data;

import java.time.LocalDateTime;
import java.util.UUID;

import com.umurcanemre.services.authorization.entity.User;
import com.umurcanemre.services.authorization.request.UserPersonalInfo;
import com.umurcanemre.services.authorization.request.UserSignUpRequest;

public abstract class TestData {
	public static UserSignUpRequest userSignUpRequest() {
		UserSignUpRequest request = new UserSignUpRequest ();
		request.setPassword(String.valueOf("password").toCharArray());
		request.setPersonalInfo(userPersonalInfo());
		return request;
	}
	public static UserPersonalInfo userPersonalInfo(){
		UserPersonalInfo personalInfo = new UserPersonalInfo();
		personalInfo.setEmail("email@email.com");
		personalInfo.setLastname("lastname");
		personalInfo.setName("name");
		return personalInfo;
	}
	public static User user() {
		User u = new User(userSignUpRequest(), "hashedPassword");
		u.setId( UUID.randomUUID().toString());
		u.setJoinTimestamp(LocalDateTime.now());
		return u;
	}
}
