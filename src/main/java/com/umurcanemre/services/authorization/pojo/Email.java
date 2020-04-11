package com.umurcanemre.services.authorization.pojo;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class Email {
	@NonNull
	private String receiver;
	private String subject;
	@NonNull
	private String body;
	
	public void checkValidity() {
		if(StringUtils.isAllBlank(this.receiver, this.body)) {
			throw new IllegalStateException();
		}
	}
}
