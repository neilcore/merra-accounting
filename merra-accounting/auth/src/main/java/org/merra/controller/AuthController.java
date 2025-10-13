package org.merra.controller;

import org.merra.api.ApiResponse;
import org.merra.dto.AuthResponse;
import org.merra.dto.JwtTokens;
import org.merra.dto.LoginRequest;
import org.merra.dto.SignupRequest;
import org.merra.dto.TokenRequest;
import org.merra.dto.VerificationResponse;
import org.merra.service.AuthService;
import org.merra.utils.AuthConstantResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/auth/")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Generate new access & refresh token
     * 
     * @param request TokenRequest object type.
     * @return
     */
    @PostMapping("tokens")
    public ResponseEntity<?> tokens(@Valid @RequestBody TokenRequest request) {
        JwtTokens tokens = authService.tokens(request);
        return new ResponseEntity<>(tokens, HttpStatus.OK);
    }

    @PostMapping("signin")
    public ResponseEntity<AuthResponse> signin(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse res = authService.login(loginRequest);
        return ResponseEntity.ok(res);
    }

    @PostMapping("signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest) {
        VerificationResponse res = authService.signup(signupRequest);

        ApiResponse response = new ApiResponse();
        if (res.resent()) {
            response.setMessage(AuthConstantResponses.EMAIL_VERIFICATION_RESEND);
            response.setResult(true);
            response.setResponse(HttpStatus.OK);
            response.setData(res);
        } else {
            response.setMessage(AuthConstantResponses.EMAIL_VERIFICATION);
            response.setResult(true);
            response.setResponse(HttpStatus.CREATED);
            response.setData(res);
        }
        return ResponseEntity.ok(response);
    }
}