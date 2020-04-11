package com.umurcanemre.services.authorization.event;

import org.springframework.context.ApplicationEvent;

import com.umurcanemre.services.authorization.entity.User;

import lombok.Getter;

@Getter
public class NewUserEvent extends ApplicationEvent {
	private static final long serialVersionUID = -5382546618377974901L;
	private final User user;
	
	public NewUserEvent(User user) {
		super(user);
		this.user = user;
	}
}
