package com.umurcanemre.services.authorization.event;

import org.springframework.context.ApplicationEvent;

import com.umurcanemre.services.authorization.entity.User;

import lombok.Getter;

@Getter
public class UserPasswordLostEvent extends ApplicationEvent {
	private static final long serialVersionUID = 7501659790204865996L;
	private final User user;
	
	public UserPasswordLostEvent(User user) {
		super(user);
		this.user = user;
	}

}
