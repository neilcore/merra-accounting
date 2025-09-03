package org.merra.utilities;

public final class AccountConstants {
	// prevent instantiation
	private AccountConstants() {}
	
	// statuses
	public static final String ACCOUNT_STATUS_ACTIVE = "ACTIVE";
	public static final String ACCOUNT_STATUS_ARCHIVED = "ARCHIVED";
	
	// Entry type
	public static final String ACC_ENTRY_DEBIT = "DEBIT";
	public static final String ACC_ENTRY_CREDIT = "CREDIT";
	
	// Account class types
	public static final String ACC__CLASS_TYPE_ASSET = "ASSET";
	public static final String ACC__CLASS_TYPE_EQUITY = "EQUITY";
	public static final String ACC__CLASS_TYPE_EXPENSE = "EXPENSE";
	public static final String ACC__CLASS_TYPE_LIABILITY = "LIABILITY";
	public static final String ACC__CLASS_TYPE_REVENUE = "REVENUE";
	
	// Default Account Codes
	// assets
	public static final String ACC_CODE_ACC_RECEIVABLE = "100";
	public static final String ACC_CODE_PREP_EXPENSES = "110";
	public static final String ACC_CODE_INVENTORY = "120";
	public static final String ACC_CODE_FIXED_ASSET = "130";
	// liabilities
	public static final String ACC_CODE_ACC_PAYABLE = "200";
	public static final String ACC_CODE_LOANS_PAYABLE = "210";
	public static final String ACC_CODE_TAX_PAYABLE = "230";
	// equities
	public static final String ACC_CODE_RETAINED_EARNING = "300";
	public static final String ACC_CODE_OWNER_DRAWING = "310";
	public static final String ACC_CODE_OWNER_CAPITAL = "320";
	// revenues
	public static final String ACC_CODE_SALES_REVENUE = "400";
	public static final String ACC_CODE_SERVICE_INCOME = "410";
	// expenses
	public static final String ACC_CODE_OFFICE_EXPENSES = "500";
	public static final String ACC_CODE_MARKETING_EXPENSES = "510";
	public static final String ACC_CODE_CONSULTING_ACCOUNTING = "520";
	public static final String ACC_CODE_COST_GOODS_SOLD = "530";
	public static final String ACC_CODE_UTILITIES = "540";
	public static final String ACC_CODE_TRAVEL_AND_ENTERTAINMENT = "550";
}
