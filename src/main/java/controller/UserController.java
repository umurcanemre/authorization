package controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.umurcanemre.services.authorization.request.SignUpRequest;
import com.umurcanemre.services.authorization.request.ValidationRequest;

//TODO: opendocs 
// valid annotation

@RestController(value = "user/")
public class UserController {

	@PostMapping(value = "signup")
	public String signup(@RequestBody SignUpRequest request) {
		return null;
		
	}

	@PostMapping(value = "validate")
	public String validate(@RequestBody ValidationRequest request) {
		return null;
		
	}
}
