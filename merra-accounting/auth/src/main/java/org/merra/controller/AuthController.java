package org.merra.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.merra.api.ApiError;
import org.merra.config.JwtUtils;
import org.merra.dto.AuthResponse;
import org.merra.dto.LoginRequest;
import org.merra.dto.SignupRequest;
import org.merra.entities.UserAccount;
import org.merra.enums.Roles;
import org.merra.repositories.UserAccountRepository;
import org.merra.services.UserAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/auth/")
@RequiredArgsConstructor
public class AuthController {
	private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserAccountRepository userRepository;
    private final UserAccountService userAccountService;
    
    @PostMapping("signin")
    public ResponseEntity<?> signin(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(),
                            loginRequest.password()
                    )
            );
        } catch (AuthenticationException e) {
        	ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Invalid credentials", List.of(e.getMessage()));
            return ResponseEntity.badRequest().body(apiError);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserAccount getUser = userRepository.findUserByEmailIgnoreCase(loginRequest.email())
        		.orElseThrow(() -> new RuntimeException("User not found with email: " + loginRequest.email()));
        
        Map<String, String> jwtTokens = jwtUtils.generateToken(getUser);
        List<String> roles = getUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        AuthResponse response = new AuthResponse(
        		jwtTokens,
        		getUser.getUsername(),
        		roles
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest) {
        Optional<UserAccount> findUserEmail = userRepository.findUserByEmailIgnoreCase(signupRequest.email());

        if (findUserEmail.isPresent()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Email already exists");
            errorResponse.put("status", false);
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        UserAccount userBuilder = UserAccount.builder()
        		.firstName(signupRequest.firstName())
        		.lastName(signupRequest.lastName())
        		.email(signupRequest.email())
        		.accountPassword(passwordEncoder.encode(signupRequest.password()))
        		.roles(Roles.NONE.toString()) // By default user account don't have a role yet
        		.build();

        UserAccount newUser = userRepository.save(userBuilder);
        
        /**
         * Once the new user is created,
         * create it's account settings
         */
        userAccountService.createUserAccountSetting(newUser);
        
        Map<String, String> jwtToken = jwtUtils.generateToken(newUser);
        List<String> roles = newUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        AuthResponse response = new AuthResponse(jwtToken, newUser.getUsername(), roles);

        return ResponseEntity.ok(response);
    }
}