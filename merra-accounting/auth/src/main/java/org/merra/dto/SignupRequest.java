package org.merra.dto;

import jakarta.validation.constraints.NotBlank;

public record SignupRequest(
		@NotBlank(message = "email component cannot be blank.") String email,
		String contactNumber,
		@NotBlank(message = "country component cannot be blank.") String country,
		@NotBlank(message = "firstName component cannot be blank.") String firstName,
		@NotBlank(message = "lastName component cannot be blank.") String lastName,
		@NotBlank(message = "password component cannot be blank.") String password) {
}