package org.merra.config;

import java.util.Optional;

import org.merra.entities.UserAccount;
import org.merra.repositories.UserAccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private final UserAccountRepository userAccountRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserAccount> findUserByEmail = userAccountRepository.findUserByEmailIgnoreCase(username);
        if (findUserByEmail.isPresent()) {
            return findUserByEmail.get();
        } else {
            throw new RuntimeException("User not found with username: " + username);
        }
	}

}
