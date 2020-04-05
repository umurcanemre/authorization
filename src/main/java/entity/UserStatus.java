package entity;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.ToString;

@Entity
@AllArgsConstructor
@ToString
public enum UserStatus {
	ACTIVE(1),
	PASSIVE(0),
	PENDING_VALIDATION(2),
	VALIDATION_FAILED(3);
	
	private int value;
}
