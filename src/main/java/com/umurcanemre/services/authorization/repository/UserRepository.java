package com.umurcanemre.services.authorization.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.umurcanemre.services.authorization.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
	Optional<User> findByEmail(String email);
	Optional<User> findByIdAndEmail(String id, String email);
}