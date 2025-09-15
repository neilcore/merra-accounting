package org.merra.api;

import java.util.List;

import org.springframework.http.HttpStatus;

import jakarta.validation.constraints.NotEmpty;

import java.util.Arrays;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public final class ApiError extends JsonResponse {
	
	@NotEmpty(message = "errors attribute cannot be empty.")
    private List<String> errors;

    public ApiError(String message, boolean result, HttpStatus response, List<String> errors) {
        super(message, result, response);
        this.errors = errors;
    }

    public ApiError(String message, Boolean result, HttpStatus response, String error) {
    	super(message, result, response);
        this.errors = Arrays.asList(error);
    }
}