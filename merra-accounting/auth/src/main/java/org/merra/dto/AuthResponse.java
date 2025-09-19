package org.merra.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthResponse(
		@NotNull(message = "token component cannot be null.")
		Tokens token,
		@NotBlank(message = "email component cannot be blank.")
		String email,
		List<String> roles
) {
	
	public record Tokens(
			@NotBlank(message = "accessToken component cannot be blank.")
			String accessToken,
			@NotBlank(message = "refreshToken component cannot be blank.")
			String refreshToken
	) {}
}