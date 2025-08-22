package org.merra.repositories;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.merra.entities.Account;
import org.merra.repositories.projections.AccountLookup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
	// Statuses
	static final String ACCOUNT_STATUS_ACTIVE = "ACTIVE";
	static final String ACCOUNT_STATUS_ARCHIVED = "ARCHIVED";
	
	// Account class types
	static final String ACC__CLASS_TYPE_ASSET = "ASSET";
	static final String ACC__CLASS_TYPE_EQUITY = "EQUITY";
	static final String ACC__CLASS_TYPE_EXPENSE = "EXPENSE";
	static final String ACC__CLASS_TYPE_LIABILITY = "LIABILITY";
	static final String ACC__CLASS_TYPE_REVENUE = "REVENUE";
	
	@Query(
			"SELECT ac.code FROM Account ac WHERE ac.code = :code " +
			"AND ac.organization = :organizationId"
	)
	Optional<AccountLookup> findAccountByCodeAndOrganization(
			@Param("code") String code,
			@Param("organizationId") UUID organizationId
	);
	
	boolean existsByCodeIgnoreCase(String code);
	
	@Query("SELECT ac FROM Account ac WHERE ac.archived = false")
	Set<Account> findAllNotArchived();
	
	@Query("SELECT ac FROM Account ac WHERE ac.organization.id = : organizationId")
	Set<Account> findAccountByOrganizationId(@Param("organizationId") UUID organizationId);
}
