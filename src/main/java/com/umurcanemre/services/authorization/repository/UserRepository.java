package com.umurcanemre.services.authorization.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.umurcanemre.services.authorization.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
	Optional<User> findByIdAndValidationCode(String email, String validationCode);
	Optional<User> findByEmail(String email);
	Optional<User> findByEmailAndPwResetCode(String email, String pwResetCode);
	
	@Query(value = "SELECT AVG(u.validation_duration_in_mins) FROM user u WHERE u.status =  ?1", nativeQuery = true)
	Double getAveregeValidationDuration(String status);

	Set<User> findUsersByJoinTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);
	Set<User> findUsersBySessionLiveAndLastLoginTimestampGreaterThan(boolean sessionLive, LocalDateTime startTime);
}