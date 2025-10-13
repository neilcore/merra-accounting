package org.merra.dto;

import jakarta.validation.constraints.NotBlank;

public record VerificationResponse(
        boolean resent,
        @NotBlank(message = "verificationToken component cannot be blank.") String verificationToken) {

}
