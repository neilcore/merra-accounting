package org.merra.repositories;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.merra.entities.Account;
import org.merra.repositories.projections.AccountLookup;
import org.merra.repositories.projections.JournalAccountLookup;
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
	
	// Default Account Codes
	static final String ACC_CODE_ACC_RECEIVABLE = "100";
	static final String ACC_CODE_PREP_EXPENSES = "110";
	static final String ACC_CODE_INVENTORY = "120";
	static final String ACC_CODE_FIXED_ASSET = "130";
	static final String ACC_CODE_ACC_PAYABLE = "200";
	static final String ACC_CODE_LOANS_PAYABLE = "210";
	static final String ACC_CODE_RETAINED_EARNING = "300";
	static final String ACC_CODE_OWNER_DRAWING = "310";
	static final String ACC_CODE_OWNER_CAPITAL = "320";
	static final String ACC_CODE_SALES_REVENUE = "400";
	static final String ACC_CODE_SERVICE_INCOME = "410";
	static final String ACC_CODE_OFFICE_EXPENSES = "500";
	static final String ACC_CODE_MARKETING_EXPENSES = "510";
	static final String ACC_CODE_CONSULTING_ACCOUNTING = "520";
	static final String ACC_CODE_COST_GOODS_SOLD = "530";
	static final String ACC_CODE_UTILITIES = "540";
	static final String ACC_CODE_TRAVEL_AND_ENTERTAINMENT = "550";
	
	@Query(
			"SELECT ac.code FROM Account ac WHERE ac.code = :code " +
			"AND ac.organization = :organizationId"
	)
	Optional<AccountLookup> findAccountByCodeAndOrganization(
			@Param("code") String code,
			@Param("organizationId") UUID organizationId
	);
	
	@Query(
			"SELECT ac.code FROM Account ac WHERE ac.code = :code " +
			"AND ac.organization = :organizationId"
	)
	Optional<JournalAccountLookup> findJournalAccountDetail(
			@Param("code") String code,
			@Param("organizationId") UUID organizationId
	);
	
	boolean existsByCodeIgnoreCase(String code);
	
	@Query("SELECT ac FROM Account ac WHERE ac.archived = false")
	Set<Account> findAllNotArchived();
	
	@Query("SELECT ac FROM Account ac WHERE ac.organization.id = : organizationId")
	Set<Account> findAccountByOrganizationId(@Param("organizationId") UUID organizationId);
}
