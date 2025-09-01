package org.merra.services;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.merra.entities.UserAccount;
import org.merra.entities.UserAccountSettings;
import org.merra.enums.Roles;
import org.merra.repositories.UserAccountRepository;
import org.merra.repositories.UserAccountSettingsRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserAccountService {
	private final UserAccountRepository userRepository;
	private final UserAccountSettingsRepository accountSettingsRepository;
	
	// TODO - createUserAccountSetting() must test this method
	public void createUserAccountSetting(UserAccount account) {
        UserAccountSettings accountSettings = UserAccountSettings.builder()
        		.userAccount(account)
        		.build();
		try {
			accountSettingsRepository.save(accountSettings);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method will retrieve UserAccount entity by ID.
	 * @param id  - accepts {@linkplain java.util.UUID} type 
	 * @return - {@linkplain UserAccount} object type.
	 */
	public UserAccount retrieveById(UUID id) {
		Optional<UserAccount> findById = userRepository.findById(id);
		
		if (findById.isEmpty()) {
			throw new NoSuchElementException("User entity " + id + " not found.");
		}
		return findById.get();
	}
	
	/**
	 * This will retrieve current authenticated user entity
	 * @return {@linkplain UserAccount} object
	 * @exception - throw {@linkplain java.util.NoSuchElementException}
	 * if it can't find one.
	 */
	public UserAccount getAuthenticatedUser() {
		Optional<UserAccount> findAuthUser = userRepository.findAuthenticatedUser();
		
		if (findAuthUser.isEmpty()) {
			throw new NoSuchElementException("Authenticated user not found in the database.");
		}
		
		return findAuthUser.get();
	}
    
	public String retrieveRole(String role) {
		if (role == Roles.SUBSCRIBER.toString()) {
			return Roles.SUBSCRIBER.toString();
		} else if (role == Roles.READ_ONLY.toString()) {
			return Roles.READ_ONLY.toString();
		} else if (role == Roles.INVOICE_ONLY.toString()) {
			return Roles.READ_ONLY.toString();
		} else {
			return "n/a";
		}
	}
}
