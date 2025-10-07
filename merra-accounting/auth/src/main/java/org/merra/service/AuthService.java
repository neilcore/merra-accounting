package org.merra.service;

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
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityExistsException;

@Service
public class AuthService {
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserAccountRepository userRepository;
    private final UserAccountService userAccountService;

    public AuthService(
            UserDetailsService userDetailsService,
            JwtUtils jwtUtils,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            UserAccountRepository userAccountRepository,
            UserAccountService userAccountService) {
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userAccountRepository;
        this.userAccountService = userAccountService;
    }

    public AuthResponse login(@NonNull LoginRequest request) {
        Authentication authentication;

        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        } catch (AuthenticationException e) {
            throw new org.springframework.security.authentication.BadCredentialsException(
                    AuthConstantResponses.INVALID_CREDENTIALS, e);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserAccount getUser = userRepository
                .findUserByEmailIgnoreCase(request.email()).get();

        Map<String, String> jwtTokens = jwtUtils.generateToken(getUser);
        List<String> roles = getUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        return new AuthResponse(
                new JwtTokens(jwtTokens.get("accessToken"), jwtTokens.get("refreshToken")),
                new AuthResponse.UserDetail(getUser.getUserId(), getUser.getEmail()),
                roles);
    }

    public AuthResponse signup(@NonNull SignupRequest request) {
        Optional<UserAccount> findUserEmail = userRepository.findUserByEmailIgnoreCase(request.email());

        if (findUserEmail.isPresent())
            throw new EntityExistsException(AuthConstantResponses.EMAIL_EXISTS);

        UserAccount userBuilder = new UserAccount();
        userBuilder.setFirstName(request.firstName());
        userBuilder.setLastName(request.lastName());
        userBuilder.setEmail(request.email());
        userBuilder.setAccountPassword(passwordEncoder.encode(request.password()));
        userBuilder.setRoles(Roles.NONE.toString()); // By default user account don't have a

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
                new AuthResponse.UserDetail(newUser.getUserId(), newUser.getEmail()),
                roles);

        return authResponse;
    }

    public JwtTokens tokens(@NonNull TokenRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.userEmail());

        final boolean IS_TOKEN_VALID = jwtUtils.isTokenValid(request.refreshToken(), userDetails);
        if (!IS_TOKEN_VALID) {
            throw new org.springframework.security.authentication.BadCredentialsException(
                    AuthConstantResponses.INVALID_REFRESH_TOKEN);
        }

        final Map<String, String> jwtToken = jwtUtils.generateToken(userDetails);
        return new JwtTokens(
                jwtToken.get("accessToken"),
                jwtToken.get("refreshToken"));
    }
}
