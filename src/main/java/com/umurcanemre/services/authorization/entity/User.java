package com.umurcanemre.services.authorization.entity;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import com.umurcanemre.services.authorization.request.UserSignUpRequest;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class User {

	@Id
	@Column(nullable = false)
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String id;
	private String name;
	private String lastName;

	@Column(unique = true, nullable = false)
	private String email;
	
	@Enumerated(EnumType.STRING)
	private UserStatus status;

	@CreationTimestamp
	private LocalDateTime joinTimestamp;
	
	@Column(nullable = true)
	private LocalDateTime validationTimestamp;

	@OneToOne(cascade=CascadeType.PERSIST)
	private ValidationCode validationCode;
	
	public User(UserSignUpRequest request) {
		this.update(request);
		this.status = UserStatus.PENDING_VALIDATION;
		this.validationCode = new ValidationCode();
	}
	
	public void update(UserSignUpRequest request) {
		this.name = request.getName();
		this.lastName = request.getLastname();
		this.email = request.getEmail();
	}
	
	public void validate() {
		this.status = UserStatus.ACTIVE;
		this.validationTimestamp = LocalDateTime.now();
		this.validationCode = null;
	}
}
