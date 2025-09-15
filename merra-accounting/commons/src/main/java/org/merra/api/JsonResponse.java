package org.merra.api;

import org.springframework.http.HttpStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
sealed class JsonResponse permits ApiError, ApiResponse {
	
	@NotBlank(message = "message attribute cannot be blank.")
	private String message;
	
	@NotNull(message = "result attribute cannot be null.")
	private boolean result;
	
	@NotNull(message = "response attribute cannot be null.")
	private HttpStatus response;
	
	public JsonResponse(String msg, boolean result, HttpStatus status) {
		this.message = msg.isBlank() ? "Request sent successfully" : msg;
		this.result = result;
		this.response = status;
	}
}