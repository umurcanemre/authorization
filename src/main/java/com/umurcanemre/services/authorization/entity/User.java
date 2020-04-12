package com.umurcanemre.services.authorization.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import com.umurcanemre.services.authorization.request.UserPersonalInfo;
import com.umurcanemre.services.authorization.request.UserSignUpRequest;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class User implements Serializable {
	private static final long serialVersionUID = 1131346119576712828L;
	
	@Id
	@Column(nullable = false)
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String id;
	private String name;
	private String lastName;
	private String password;

	@Column(unique = true, nullable = false)
	private String email;
	
	@Enumerated(EnumType.STRING)
	private UserStatus status;

	@CreationTimestamp
	private LocalDateTime joinTimestamp;
	
	@UpdateTimestamp
	private LocalDateTime updateTimestamp;

	@Column(nullable = true)
	private LocalDateTime validationTimestamp;
	@Column(nullable = true)
	private int validationDurationInMins;
	@Column(nullable = true)
	private String validationCode;
	
	@Column(nullable = true)
	private LocalDateTime pwResetCodeTimestamp;
	@Column(nullable = true)
	private String pwResetCode;

	private boolean sessionLive;
	@Column(nullable = true)
	private LocalDateTime lastLoginTimestamp;
	
	
	public User(UserSignUpRequest request,String password) {
		this.update(request.getPersonalInfo());
		this.password = password;
		this.status = UserStatus.PENDING_VALIDATION;
		this.validationCode = UUID.randomUUID().toString();
	}
	
	public void update(UserPersonalInfo request) {
		this.name = request.getName();
		this.lastName = request.getLastname();
		this.email = request.getEmail();
	}
	
	public void validate() {
		this.status = UserStatus.ACTIVE;
		this.validationTimestamp = LocalDateTime.now();
		this.validationCode = null;
		this.validationDurationInMins = (int)this.joinTimestamp.until(validationTimestamp, ChronoUnit.MINUTES);
	}
	
	public void createPWResetCode() {
		pwResetCodeTimestamp = LocalDateTime.now();
		pwResetCode = UUID.randomUUID().toString();
	}
	
	public void login() {
		this.sessionLive = true;
		this.lastLoginTimestamp = LocalDateTime.now();
	}
	
	public void logout() {
		this.sessionLive = false;
	}
}
