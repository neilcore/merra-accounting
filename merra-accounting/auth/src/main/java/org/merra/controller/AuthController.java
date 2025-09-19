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
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/auth/")
@RequiredArgsConstructor
public class AuthController {
	private final UserDetailsService userDetailsService;
	private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserAccountRepository userRepository;
    private final UserAccountService userAccountService;
    
    @PostMapping("tokens")
    public ResponseEntity<?> tokens(@Valid @RequestBody TokenRequest request) {
    	UserDetails userDetails = userDetailsService.loadUserByUsername(request.userEmail());
    	
    	final boolean IS_TOKEN_VALID = jwtUtils.isTokenValid(request.refreshToken(), userDetails);
        if (!IS_TOKEN_VALID) return new ResponseEntity<>(AuthConstantResponses.INVALID_REFRESH_TOKEN, HttpStatus.UNAUTHORIZED);
        
        final Map<String, String> jwtToken = jwtUtils.generateToken(userDetails);
        JwtTokens tokens = new JwtTokens(
        		jwtToken.get("accessToken"),
        		jwtToken.get("refreshToken")
        );
        
        return new ResponseEntity<>(tokens, HttpStatus.OK);
    }
    
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
        	ApiError apiError = new ApiError(
        			AuthConstantResponses.INVALID_CREDENTIALS,
        			false,
        			HttpStatus.BAD_REQUEST,
        			List.of(e.getMessage())
        	);
            return ResponseEntity.badRequest().body(apiError);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserAccount getUser = userRepository
        		.findUserByEmailIgnoreCase(loginRequest.email()).get();
        
        Map<String, String> jwtTokens = jwtUtils.generateToken(getUser);
        List<String> roles = getUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        AuthResponse authResponse = new AuthResponse(
        		new JwtTokens(jwtTokens.get("accessToken"), jwtTokens.get("refreshToken")),
        		getUser.getUsername(),
        		roles
        );
        
        ApiResponse res = new ApiResponse(
        		AuthConstantResponses.LOGIN_SUCCESSFUL,
        		true,
        		HttpStatus.OK,
        		authResponse
        );
        return ResponseEntity.ok(res);
    }

    @PostMapping("signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest) {
        Optional<UserAccount> findUserEmail = userRepository.findUserByEmailIgnoreCase(signupRequest.email());

        if (findUserEmail.isPresent()) {
            ApiError apiError = new ApiError(
            		AuthConstantResponses.SIGNUP_FAILED,
            		false,
            		HttpStatus.CONFLICT,
            		AuthConstantResponses.EMAIL_EXISTS
            );
            return ResponseEntity.badRequest().body(apiError);
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
        AuthResponse authResponse = new AuthResponse(
        		new JwtTokens(jwtToken.get("accessToken"), jwtToken.get("refreshToken")),
        		newUser.getUsername(),
        		roles
        );

        ApiResponse response = new ApiResponse();
        response.setMessage(AuthConstantResponses.ACCOUNT_CREATED);
        response.setResult(true);
        response.setResponse(HttpStatus.CREATED);
        response.setData(authResponse);
        return ResponseEntity.ok(response);
    }
}