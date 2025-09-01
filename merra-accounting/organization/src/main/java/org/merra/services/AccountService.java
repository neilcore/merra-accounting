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
import org.merra.entities.Invoice;
import org.merra.entities.Organization;
import org.merra.mapper.AccountMapper;
import org.merra.repositories.AccountCategoryRepository;
import org.merra.repositories.AccountRepository;
import org.merra.repositories.AccountTypeRepository;
import org.merra.repositories.InvoiceRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

// This class will handle the chart of accounts
@Service
@RequiredArgsConstructor
public class AccountService {
	private final AccountMapper accountMapper;
	private final AccountCategoryRepository accountCategoryRepository;
	private final AccountRepository accountRepository;
	private final AccountTypeRepository accountTypeRepository;
	private final InvoiceRepository invoiceRepository;
	
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
	public void createDefaultAccounts(@NotNull Organization org) {
		
		// Account categories
		AccountCategory assetCat = accountCategoryRepository.findById(AccountCategoryRepository.assetId).get();
		AccountCategory liabilityCat = accountCategoryRepository.findById(AccountCategoryRepository.liabilityId).get();
		AccountCategory revenueCat = accountCategoryRepository.findById(AccountCategoryRepository.revenueId).get();
		AccountCategory equityCat = accountCategoryRepository.findById(AccountCategoryRepository.equityId).get();
		AccountCategory expenseCat = accountCategoryRepository.findById(AccountCategoryRepository.expenseId).get();
		
		// Create asset accounts
		// A debit balance account
		AccountType currentAsset = accountTypeRepository.save(new AccountType("Current Asset", ""));
		Account accountReceivable = new Account(
				org,
				AccountRepository.ACC_CODE_ACC_RECEIVABLE,
				"Receivable Account",
				currentAsset,
				assetCat
		);
		accountReceivable.setDescription("Money owed to the busines.");
		
		Account preparedExpense = new Account(
				org,
				AccountRepository.ACC_CODE_PREP_EXPENSES,
				"Prepared Expenses",
				currentAsset,
				assetCat
		);
		preparedExpense.setDescription("Expenses paid in advance.");
		
		AccountType inventoryType = accountTypeRepository.save(new AccountType("Inventory", ""));
		Account inventoryAssetAccount = new Account(
				org,
				AccountRepository.ACC_CODE_INVENTORY,
				"Inventory",
				inventoryType,
				assetCat
		);
		inventoryAssetAccount.setDescription("Account for businesses that hold and track stock.");
		
		AccountType fixedAssetType = accountTypeRepository.save(new AccountType("Fixed Assets", ""));
		Account fixedAssetAccount = new Account(
				org,
				AccountRepository.ACC_CODE_FIXED_ASSET,
				"Fixed Asset",
				fixedAssetType,
				assetCat
		);
		fixedAssetAccount.setDescription("Accounts for long-term assets.");
		
		accountRepository.saveAll(List.of(accountReceivable, preparedExpense, inventoryAssetAccount, fixedAssetAccount));
		
		// Create liability accounts
		AccountType currentLiabilityType = accountTypeRepository.save(new AccountType("Current Liability", ""));
		Account accountPayable = new Account(
				org,
				AccountRepository.ACC_CODE_ACC_PAYABLE,
				"Payable Account",
				currentLiabilityType,
				liabilityCat
		);
		accountPayable.setDescription("Tracks money you owe to your suppliers.");
		
		AccountType liabilityType = accountTypeRepository.save(new AccountType("Liability", ""));
		Account loans = new Account(
				org,
				"210",
				AccountRepository.ACC_CODE_LOANS_PAYABLE,
				liabilityType,
				liabilityCat
		);
		loans.setDescription("Long-term debt.");
		
		accountRepository.saveAll(List.of(accountPayable, loans));
		
		// Create Equity accounts
		AccountType equityType = accountTypeRepository.save(new AccountType("Equity", ""));
		Account annualEarning = new Account(
				org,
				AccountRepository.ACC_CODE_RETAINED_EARNING,
				"Retained Earnings",
				equityType,
				equityCat
		);
		annualEarning.setDescription("Profits retained in the busines.");
		
		// This is the money (or assets) the owner takes out of the business for personal use e.g. money withdrawal.
		Account ownersDrawing = new Account(
				org,
				"310",
				AccountRepository.ACC_CODE_OWNER_DRAWING,
				equityType,
				equityCat
		);
		ownersDrawing.setDescription("Owner withdrawals.");
		
		Account ownerCapital = new Account(
				org,
				"320",
				AccountRepository.ACC_CODE_OWNER_CAPITAL,
				equityType,
				equityCat
		);
		ownerCapital.setDescription("Money the owner puts into the business.");
		
		accountRepository.saveAll(List.of(annualEarning, ownersDrawing, ownerCapital));
		
		// Create revenue accounts
		AccountType revenueType = accountTypeRepository.save(new AccountType("Revenue", ""));
		Account salesRevenueAcc = new Account(
				org,
				"400",
				AccountRepository.ACC_CODE_SALES_REVENUE,
				revenueType,
				revenueCat
		);
		salesRevenueAcc.setDescription("Income from core operations.");
		
		Account serviceIncomeAcc = new Account(
				org,
				AccountRepository.ACC_CODE_SERVICE_INCOME,
				"Service Income",
				revenueType,
				revenueCat
		);
		serviceIncomeAcc.setDescription("Fees for services provided.");
		
		accountRepository.saveAll(List.of(salesRevenueAcc, serviceIncomeAcc));
		
		// Create expenses accounts
		AccountType expenseType = accountTypeRepository.save(new AccountType("Expense", ""));
		Account officeExpenses = new Account(
				org,
				AccountRepository.ACC_CODE_OFFICE_EXPENSES,
				"Office Expenses",
				expenseType,
				expenseCat
		);
		officeExpenses.setDescription("Costs that a business incurs for the day-to-day operation and maintenance of its workspace.");
		
		Account advertisingExpenses = new Account(
				org,
				AccountRepository.ACC_CODE_MARKETING_EXPENSES,
				"Marketing Expense",
				expenseType,
				expenseCat
		);
		advertisingExpenses.setDescription("Costs a business incurs to promote its products, services, or brand to a target audience.");
		
		Account consultingAndAccountingExpenses = new Account(
				org,
				AccountRepository.ACC_CODE_CONSULTING_ACCOUNTING,
				"Consulting & Accounting",
				expenseType,
				expenseCat
		);
		consultingAndAccountingExpenses.setDescription("Costs a business incurs for professional services from external experts.");
		
		AccountType directCost = accountTypeRepository.save(new AccountType("Direct Costs", ""));
		Account costOfGoodsSold = new Account(
				org,
				AccountRepository.ACC_CODE_COST_GOODS_SOLD,
				"Cost of Goods Sold",
				directCost,
				expenseCat
		);
		costOfGoodsSold.setDescription("Cost of materials / production.");
		
		Account utilities = new Account(
				org,
				AccountRepository.ACC_CODE_UTILITIES,
				"Utilities",
				expenseType,
				expenseCat
		);
		utilities.setDescription("Electricity, water, etc.");
		
		Account travelAndEntertainment = new Account(
				org,
				AccountRepository.ACC_CODE_TRAVEL_AND_ENTERTAINMENT,
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