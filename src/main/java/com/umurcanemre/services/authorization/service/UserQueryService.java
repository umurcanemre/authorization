package com.umurcanemre.services.authorization.service;

import java.time.LocalDate;

import com.umurcanemre.services.authorization.response.UserResponse;

public interface UserQueryService {
	UserResponse getUser(String id);
	Double getAverageValidationDuration();
	Integer getNewUserCountWithinDates(LocalDate startdate, LocalDate endDate);
	Integer getActiveSessionsCount();
}
