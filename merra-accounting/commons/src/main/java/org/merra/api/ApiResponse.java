package org.merra.api;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public final class ApiResponse extends JsonResponse {
	private Object data;
	
	public ApiResponse(String message, boolean result, HttpStatus status, Object data) {
		super(message, result, status);
		this.data = data;
	}
}