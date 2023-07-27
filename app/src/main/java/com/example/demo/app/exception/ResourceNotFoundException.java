package com.example.demo.app.exception;

import javax.validation.constraints.NotEmpty;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException(@NotEmpty final String message) {
		super(message);
	}

	public ResourceNotFoundException(@NotEmpty final String message, final Throwable cause) {
		super(message, cause);
	}
}
