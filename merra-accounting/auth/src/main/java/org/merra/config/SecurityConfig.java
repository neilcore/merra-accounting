package org.merra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final AuthEntrypointJwt unathorizedHandler;
	private final AuthTokenFilter authTokenFilter;
	private final CustomUserDetailsService customUserDetailsService;
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
		return builder.getAuthenticationManager();
	}
	
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(customUserDetailsService);
        /* Tell AuthenticationProvider which UserDetailService to use */
        /*
         * Fetch information about the user
         * Provide a password on encoder
         * */
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
	
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }
    
    /**
     * This security filter chain method is used for the apis.
     * @param http - accepts {@linkplain HttpSecurity} object.
     * @return - {@linkplain SecurityFilterChain} object.
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
        		.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                request ->
                        request
                                .requestMatchers(
                                		"/", "/api/auth/**",
                                		"/swagger-ui/**","/api-docs/**","/v3/api-docs/**"
                                )
                                .permitAll()
                                .anyRequest()
                                .authenticated()

        ).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unathorizedHandler))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    
//    @Bean
//    public SecurityFilterChain oauth2LoginFilterChain(HttpSecurity http) throws Exception {
//    	http
//    		.oauth2Login(null);
//    	
//    	return http.build();
//    }
}
