package com.umurcanemre.services.authorization.response;

import java.time.LocalDateTime;

import com.umurcanemre.services.authorization.entity.User;
import com.umurcanemre.services.authorization.entity.UserStatus;

import lombok.Data;

@Data
public class UserResponse {
	private String id;
	private String name;
	private String lastName;
	private String email;
	private UserStatus status;
	private LocalDateTime joinTimestamp;
	private LocalDateTime updateTimestamp;
	private LocalDateTime validationTimestamp;
	private int validationDurationInMins;
	private String validationCode;
	private boolean sessionLive;
	private LocalDateTime lastLoginTimestamp;

	public UserResponse(User user) {
		this.id = user.getId();
		this.name = user.getName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.status = user.getStatus();
		this.joinTimestamp = user.getJoinTimestamp();
		this.updateTimestamp = user.getUpdateTimestamp();
		this.validationTimestamp = user.getValidationTimestamp();
		this.validationDurationInMins = user.getValidationDurationInMins();
		this.validationCode = user.getValidationCode();
		this.sessionLive = user.isSessionLive();
		this.lastLoginTimestamp = user.getLastLoginTimestamp();
	}
}
