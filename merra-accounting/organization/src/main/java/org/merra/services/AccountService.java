package org.merra.services;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.merra.dto.AccountByOrganizationResponse;
import org.merra.dto.AccountCodeExistsResponse;
import org.merra.dto.AccountResponse;
import org.merra.entities.Account;
import org.merra.entities.AccountCategory;
import org.merra.entities.AccountType;
import org.merra.entities.Organization;
import org.merra.mapper.AccountMapper;
import org.merra.repositories.AccountCategoryRepository;
import org.merra.repositories.AccountRepository;
import org.merra.repositories.AccountTypeRepository;
import org.merra.repositories.OrganizationRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
	private final AccountMapper accountMapper;
	private final AccountCategoryRepository accountCategoryRepository;
	private final AccountRepository accountRepository;
	private final AccountTypeRepository accountTypeRepository;
	private final OrganizationRepository organizationRepository;
	
	public AccountResponse createAccount() {
		return null;
	}
	
	/**
	 * This will check if account code already exists or not
	 * @param code - accepts {@linkplain java.util.String} object type.
	 * @return - {@linkplain AccountCodeExistsResponse} object type.
	 */
	public AccountCodeExistsResponse checkIfCodeExists(String code) {
		boolean checkIfExists = accountRepository.existsByCodeIgnoreCase(code);
		
		return new AccountCodeExistsResponse(code, checkIfExists);
	}
	
	
	@Transactional
	public void createDefaultAccounts(@NotNull UUID organizationId) {
		Organization getOrganization = organizationRepository.findById(organizationId).get();
		
		// Account categories
		AccountCategory assetCat = accountCategoryRepository.findById(AccountCategoryRepository.assetId).get();
		AccountCategory liabilityCat = accountCategoryRepository.findById(AccountCategoryRepository.liabilityId).get();
		AccountCategory revenueCat = accountCategoryRepository.findById(AccountCategoryRepository.revenueId).get();
		AccountCategory equityCat = accountCategoryRepository.findById(AccountCategoryRepository.equityId).get();
		AccountCategory expenseCat = accountCategoryRepository.findById(AccountCategoryRepository.expenseId).get();
		
		// Create asset accounts
		AccountType currentAsset = accountTypeRepository.save(new AccountType("Current Asset", ""));
		Account accountReceivable = new Account(
				getOrganization,
				"100",
				"Receivable Account",
				currentAsset,
				assetCat
		);
		accountReceivable.setDescription("Money owed to the busines.");
		
		Account preparedExpense = new Account(
				getOrganization,
				"103",
				"Prepared Expenses",
				currentAsset,
				assetCat
		);
		preparedExpense.setDescription("Expenses paid in advance.");
		
		AccountType inventoryType = accountTypeRepository.save(new AccountType("Inventory", ""));
		Account inventoryAssetAccount = new Account(
				getOrganization,
				"101",
				"Inventory",
				inventoryType,
				assetCat
		);
		inventoryAssetAccount.setDescription("Account for businesses that hold and track stock.");
		
		AccountType fixedAssetType = accountTypeRepository.save(new AccountType("Fixed Assets", ""));
		Account fixedAssetAccount = new Account(
				getOrganization,
				"102",
				"Fixed Asset",
				fixedAssetType,
				assetCat
		);
		fixedAssetAccount.setDescription("Accounts for long-term assets.");
		
		accountRepository.saveAll(List.of(accountReceivable, preparedExpense, inventoryAssetAccount, fixedAssetAccount));
		
		// Create liability accounts
		AccountType currentLiabilityType = accountTypeRepository.save(new AccountType("Current Liability", ""));
		Account accountPayable = new Account(
				getOrganization,
				"300",
				"Payable Account",
				currentLiabilityType,
				liabilityCat
		);
		accountPayable.setDescription("Tracks money you owe to your suppliers.");
		
		AccountType liabilityType = accountTypeRepository.save(new AccountType("Liability", ""));
		Account loans = new Account(
				getOrganization,
				"302",
				"Loans Payable",
				liabilityType,
				liabilityCat
		);
		loans.setDescription("Long-term debt.");
		
		accountRepository.saveAll(List.of(accountPayable, loans));
		
		// Create Equity accounts
		AccountType equityType = accountTypeRepository.save(new AccountType("Equity", ""));
		Account annualEarning = new Account(
				getOrganization,
				"600",
				"Retained Earnings",
				equityType,
				equityCat
		);
		annualEarning.setDescription("Profits retained in the busines.");
		
		Account ownersDrawing = new Account(
				getOrganization,
				"601",
				"Owner’s Drawings",
				equityType,
				equityCat
		);
		ownersDrawing.setDescription("Owner withdrawals.");
		
		accountRepository.saveAll(List.of(annualEarning, ownersDrawing));
		
		// Create revenue accounts
		AccountType revenueType = accountTypeRepository.save(new AccountType("Revenue", ""));
		Account salesRevenueAcc = new Account(
				getOrganization,
				"200",
				"Sales Revenue",
				revenueType,
				revenueCat
		);
		salesRevenueAcc.setDescription("Income from core operations.");
		
		Account serviceIncomeAcc = new Account(
				getOrganization,
				"201",
				"Service Income",
				revenueType,
				revenueCat
		);
		serviceIncomeAcc.setDescription("Fees for services provided.");
		
		accountRepository.saveAll(List.of(salesRevenueAcc, serviceIncomeAcc));
		
		// Create expenses accounts
		AccountType expenseType = accountTypeRepository.save(new AccountType("Expense", ""));
		Account officeExpenses = new Account(
				getOrganization,
				"300",
				"Office Expenses",
				expenseType,
				expenseCat
		);
		officeExpenses.setDescription("Costs that a business incurs for the day-to-day operation and maintenance of its workspace.");
		
		Account advertisingExpenses = new Account(
				getOrganization,
				"301",
				"Marketing Expense",
				expenseType,
				expenseCat
		);
		advertisingExpenses.setDescription("Costs a business incurs to promote its products, services, or brand to a target audience.");
		
		Account consultingAndAccountingExpenses = new Account(
				getOrganization,
				"302",
				"Consulting & Accounting",
				expenseType,
				expenseCat
		);
		consultingAndAccountingExpenses.setDescription("Costs a business incurs for professional services from external experts.");
		
		AccountType directCost = accountTypeRepository.save(new AccountType("Direct Costs", ""));
		Account costOfGoodsSold = new Account(
				getOrganization,
				"303",
				"Cost of Goods Sold",
				directCost,
				expenseCat
		);
		costOfGoodsSold.setDescription("Cost of materials / production.");
		
		Account utilities = new Account(
				getOrganization,
				"304",
				"Utilities",
				expenseType,
				expenseCat
		);
		utilities.setDescription("Electricity, water, etc.");
		
		Account travelAndEntertainment = new Account(
				getOrganization,
				"305",
				"Travel & Entertainment",
				expenseType,
				expenseCat
		);
		travelAndEntertainment.setDescription("Business travel, meals.");
		
		accountRepository.saveAll(List.of(
				officeExpenses, advertisingExpenses,
				consultingAndAccountingExpenses, costOfGoodsSold,
				utilities, travelAndEntertainment
		));
	}
	
	/**
	 * This method will filter account objects by organization id.
	 * @param organizationId - accepts {@linkplain java.util.UUID} object type.
	 * @return - {@linkplain java.util.Set} that holds {@linkplain AccountByOrganizationResponse} objects.
	 */
	public Set<AccountByOrganizationResponse> getAccountsByOrganization(@NotNull UUID organizationId) {
		Set<Account> getOrganizationAccounts = accountRepository.findAccountByOrganizationId(organizationId);
		return accountMapper.toAccountByOrganizationResponses(getOrganizationAccounts);
	}
}