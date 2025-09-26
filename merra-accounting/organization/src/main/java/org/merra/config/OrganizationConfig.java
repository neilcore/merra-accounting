package org.merra.config;

import java.util.Set;

import org.merra.entities.AccountCategory;
import org.merra.repositories.AccountCategoryRepository;
import org.merra.repositories.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrganizationConfig implements CommandLineRunner {

    private final AccountRepository accountRepository;
	private final AccountCategoryRepository accountCategoryRepository;

	public OrganizationConfig(
			AccountRepository accountRepository,
			AccountCategoryRepository accountCategoryRepository
	) {
		this.accountRepository = accountRepository;
		this.accountCategoryRepository = accountCategoryRepository;
	}
	
	@Override
	public void run(String... args) throws Exception {
		// create account categories
		if (accountCategoryRepository.findAll().isEmpty()) {
			accountCategoryRepository.saveAll(Set.of(
					new AccountCategory("ASSET"),
					new AccountCategory("EQUITY"),
					new AccountCategory("EXPENSE"),
					new AccountCategory("LIABILITY"),
					new AccountCategory("REVENUE")
			));
		}
		
	}

}
