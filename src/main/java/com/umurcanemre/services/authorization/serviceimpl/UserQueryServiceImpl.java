package com.umurcanemre.services.authorization.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.umurcanemre.services.authorization.entity.User;
import com.umurcanemre.services.authorization.repository.UserRepository;
import com.umurcanemre.services.authorization.service.UserQueryService;

import lombok.Data;

@Service
@Data
public class UserQueryServiceImpl implements UserQueryService {
	@Autowired
	private UserRepository repository;
	
	
	@Override
	public User getUser(String id) {
		return repository.findById(id).orElse(null) ;
	}

	@Override
	public Iterable<User> getUsers(Iterable<String> idCollection) {
		return repository.findAllById(idCollection);
	}

}
