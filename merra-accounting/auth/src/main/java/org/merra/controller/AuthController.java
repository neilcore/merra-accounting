package org.merra.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.merra.api.ApiError;
import org.merra.api.ApiResponse;
import org.merra.config.JwtUtils;
import org.merra.dto.AuthResponse;
import org.merra.dto.JwtTokens;
import org.merra.dto.LoginRequest;
import org.merra.dto.SignupRequest;
import org.merra.dto.TokenRequest;
import org.merra.entities.UserAccount;
import org.merra.enums.Roles;
import org.merra.repositories.UserAccountRepository;
import org.merra.service.AuthService;
import org.merra.services.UserAccountService;
import org.merra.utils.AuthConstantResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/auth/")
public class AuthController {
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserAccountRepository userRepository;
    private final UserAccountService userAccountService;

    private final AuthService authService;

    public AuthController(
            AuthService authService,
            UserDetailsService userDetailsService,
            JwtUtils jwtUtils,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            UserAccountRepository userRepository,
            UserAccountService userAccountService) {
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userAccountService = userAccountService;
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
        AuthResponse authResponse = authService.signup(signupRequest);

        ApiResponse response = new ApiResponse();
        response.setMessage(AuthConstantResponses.ACCOUNT_CREATED);
        response.setResult(true);
        response.setResponse(HttpStatus.CREATED);
        response.setData(authResponse);
        return ResponseEntity.ok(response);
    }
}