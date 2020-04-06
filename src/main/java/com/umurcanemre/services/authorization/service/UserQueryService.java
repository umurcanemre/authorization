package com.umurcanemre.services.authorization.service;

import com.umurcanemre.services.authorization.entity.User;

public interface UserQueryService {
	User getUser(String id);
	Iterable<User> getUsers(Iterable<String> idCollection);
}
