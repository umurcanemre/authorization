package com.umurcanemre.services.authorization.service;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;

import com.umurcanemre.services.authorization.data.TestData;
import com.umurcanemre.services.authorization.entity.User;
import com.umurcanemre.services.authorization.event.NewUserEvent;
import com.umurcanemre.services.authorization.exception.OperationAllowedTimeOverException;
import com.umurcanemre.services.authorization.exception.UserValidationDontMatchException;
import com.umurcanemre.services.authorization.repository.UserRepository;
import com.umurcanemre.services.authorization.request.UserSignUpRequest;
import com.umurcanemre.services.authorization.response.UserResponse;
import com.umurcanemre.services.authorization.serviceimpl.UserCommandServiceImpl;

public class UserCommandServiceImplTest {
	private static final UserCommandServiceImpl service = new UserCommandServiceImpl();
	private static final UserRepository repository = Mockito.mock(UserRepository.class);
	private static final Environment env = Mockito.mock(Environment.class);
	private static final ApplicationEventPublisher applicationEventPublisher = Mockito.mock(ApplicationEventPublisher.class);

	@BeforeAll
	public static void initiate() {
		service.setApplicationEventPublisher(applicationEventPublisher);
		service.setEnv(env);
		service.setRepository(repository);
	}
	
	@Test
	public void createUserTest() {
		UserSignUpRequest req = TestData.userSignUpRequest();

		when(repository.save(any(User.class))).thenReturn(TestData.user());
		doNothing().when(applicationEventPublisher).publishEvent(any(NewUserEvent.class));
		when(env.getProperty("bcrypt.iteration")).thenReturn("5");

		UserResponse resp = service.createUser(req);
		assertNotNull(resp);
		assertNotEquals(req.getPersonalInfo().getEmail(), resp.getEmail());
	}

	@Test
	public void validateUser() {
		User user = TestData.user();

		when(repository.findByIdAndValidationCode(user.getId(), user.getValidationCode()))
				.thenReturn(Optional.of(user));
		when(env.getProperty("validation.allowedtime.duration")).thenReturn("1440");
		when(env.getProperty("validation.allowedtime.unit")).thenReturn("MINUTES");
		when(repository.save(any(User.class))).thenReturn(user);

		service.validateUser(user.getId(), user.getValidationCode());
	}

	@Test
	public void validateUser_UserNotFound() {
		User user = TestData.user();

		when(repository.findByIdAndValidationCode(user.getId(), user.getValidationCode()))
				.thenReturn(Optional.ofNullable(null));

		assertThrows(UserValidationDontMatchException.class,
				() -> service.validateUser(user.getId(), user.getValidationCode()));
	}

	@Test
	public void validateUser_ValidationTimeOut() {
		User user = TestData.user();

		when(repository.findByIdAndValidationCode(user.getId(), user.getValidationCode()))
				.thenReturn(Optional.of(user));
		when(env.getProperty("validation.allowedtime.duration")).thenReturn("1");
		when(env.getProperty("validation.allowedtime.unit")).thenReturn("NANOS");

		assertThrows(OperationAllowedTimeOverException.class,
				() -> service.validateUser(user.getId(), user.getValidationCode()));
	}
}
