package com.umurcanemre.services.authorization.serviceimpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.umurcanemre.services.authorization.entity.UserStatus;
import com.umurcanemre.services.authorization.exception.UserNotFoundException;
import com.umurcanemre.services.authorization.repository.UserRepository;
import com.umurcanemre.services.authorization.response.UserResponse;
import com.umurcanemre.services.authorization.service.UserQueryService;

import lombok.Data;

@Service
@Data
public class UserQueryServiceImpl implements UserQueryService {
	@Autowired
	private UserRepository repository;
	@Autowired
	private Environment env;
	
	
	@Override
	public UserResponse getUser(String id) {
		return new UserResponse(repository.findById(id).orElseThrow(()-> new UserNotFoundException(true, id)));
	}
	@Override
	public Double getAverageValidationDuration() {
		return repository.getAveregeValidationDuration(UserStatus.ACTIVE.toString());
	}

	@Override
	public Integer getNewUserCountWithinDates(LocalDate startdate, LocalDate endDate) {
		return repository.findUsersByJoinTimestampBetween(startdate.atStartOfDay(), endDate.plusDays(1).atStartOfDay()).size();
	}

	@Override
	public Integer getActiveSessionsCount() {
		long allowedTime = Long.parseLong(env.getProperty("pwreset.allowedtime.duration"));
		ChronoUnit cu = ChronoUnit.valueOf(env.getProperty("pwreset.allowedtime.unit"));
		LocalDateTime loginLimit = LocalDateTime.now().minus(allowedTime, cu);
		return repository.findUsersBySessionLiveAndLastLoginTimestampGreaterThan(true, loginLimit).size();
	}
	@Override
	public Integer getFailedValidationCount() {
		long allowedTime = Long.parseLong(env.getProperty("validation.allowedtime.duration"));
		ChronoUnit cu = ChronoUnit.valueOf(env.getProperty("validation.allowedtime.unit"));
		LocalDateTime validationTimeLimit = LocalDateTime.now().minus(allowedTime, cu);
		return repository.findUsersByStatusAndJoinTimestampLessThan(UserStatus.PENDING_VALIDATION, validationTimeLimit).size();
	}

}
