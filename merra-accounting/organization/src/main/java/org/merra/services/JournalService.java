package org.merra.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.merra.entities.Account;
import org.merra.entities.Invoice;
import org.merra.entities.Journal;
import org.merra.entities.JournalLine;
import org.merra.entities.LineItem;
import org.merra.entities.Organization;
import org.merra.entities.embedded.AccountDetail;
import org.merra.entities.embedded.JournalTotalAmountEntry;
import org.merra.entities.embedded.LineItemByAccountCode;
import org.merra.entities.embedded.TaxDetail;
import org.merra.exceptions.OrganizationExceptions;
import org.merra.repositories.AccountRepository;
import org.merra.repositories.InvoiceRepository;
import org.merra.utilities.AccountConstants;
import org.merra.utilities.InvoiceConstants;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JournalService {
	private final AccountRepository accountRepository;
	
	public void entry(
			@NotEmpty Set<LineItem> lineItems,
			Organization org,
			@NotNull Invoice findInvoiceById
	) {
		List<LineItemByAccountCode> getLineItemByAccountCode = LineItem.getLineItemByAccountCode(lineItems);
		
		Journal createJournal = new Journal();
		// set the journal date
		createJournal.setJournalDate(findInvoiceById.getDate());
		
		/**
		 * Get the accounts receivable or debit account
		 * if it is a customer invoice
		 */
		String CUSTOMER_INVOICE = InvoiceConstants.INVOICE_TYPE_CUSTOMER_INVOICE;
		JournalLine debitJournalLine = null;
		
		if (findInvoiceById.getType().equalsIgnoreCase(CUSTOMER_INVOICE)) {
			Account getDebitAccount = accountRepository
					.findByAccountCodeAndOrganizationId(AccountConstants.ACC_CODE_ACC_RECEIVABLE, org.getId())
					.orElseThrow(()-> new EntityNotFoundException(OrganizationExceptions.NOT_FOUND_ACCOUNT));
			
			debitJournalLine = buildJournalLine(
					createJournal,
					getDebitAccount,
					findInvoiceById.getGrandTotal()
			);
			TaxDetail debitTaxDetail = new TaxDetail(new BigDecimal("0.00"), null, ""); // TODO - needs improvement
			debitJournalLine.setTaxDetail(debitTaxDetail);
		}
		
		List<JournalLine> journalLines = new ArrayList<>();
		if (debitJournalLine != null) {
			journalLines.add(debitJournalLine);
		}
		
		// for tracking taxes
		// TODO - must improve or change
		if (!findInvoiceById.getLineAmountTypes().equals(InvoiceConstants.INVOICE_LINE_AMOUNT_TYPE_NOTAX)) {
			Account getTaxPayableAcc = accountRepository
					.findByAccountCodeAndOrganizationId(AccountConstants.ACC_CODE_TAX_PAYABLE, org.getId())
					.orElseThrow(()-> new EntityNotFoundException(OrganizationExceptions.NOT_FOUND_ACCOUNT));
			
			JournalLine taxJournalLine = buildJournalLine(
					createJournal,
					getTaxPayableAcc,
					findInvoiceById.getTotalTax()
			);
			journalLines.add(taxJournalLine);
		}
		
		for (LineItemByAccountCode lt: getLineItemByAccountCode) {
			Double totalLineAmount = null;
			Account getAccountCode = accountRepository
					.findByAccountCodeAndOrganizationId(lt.getAccountCode(), org.getId())
					.orElseThrow(() -> new EntityNotFoundException(OrganizationExceptions.NOT_FOUND_ACCOUNT));
			
			// If multiple line items have the same account code
			if (lt.getTotal() > 1) {
				totalLineAmount = lt.getLineItems()
						.stream()
						.mapToDouble(t -> t.getLineAmount())
						.sum();
			}else {
				totalLineAmount = lt.getLineItems().stream()
						.map(lm -> lm.getLineAmount())
						.findFirst()
						.orElse(null);
			}
			journalLines.add(buildJournalLine(
					createJournal,
					getAccountCode,
					new BigDecimal(totalLineAmount)
			));
		}
		
		createJournal.setJournalLines(journalLines);
		
		// total debit amount
		BigDecimal totalDebit = journalLines.stream()
				.filter(td -> td.getDebit() != null)
				.map(td -> td.getDebit())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		// total credit amount.
		BigDecimal totalCredit = journalLines.stream()
				.filter(tc -> tc.getCredit() != null)
				.map(tc -> tc.getCredit())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		
		JournalTotalAmountEntry total = new JournalTotalAmountEntry(totalDebit, totalCredit);
		createJournal.setTotal(total);
	}
	
	/**
	 * This method is used for building JournalLine object.
	 * @param journal - accepts {@linkplain Journal} object.
	 * @param accountCode - accepts {@linkplain java.util.String}
	 * @param totalLineAmount - accepts {@linkplain Double}
	 * @return - {@linkplain JournalLine} object.
	 */
	private JournalLine buildJournalLine(
			Journal journal,
			@NotNull Account accountCode,
			BigDecimal totalLineAmount
	) {
		JournalLine jl = new JournalLine();
		
		jl.setJournal(journal);
		jl.setAccountDetails(new AccountDetail(
				accountCode.getAccountId(),
				accountCode.getCode(),
				accountCode.getAccountType().getName(),
				accountCode.getAccountName()
		));
		jl.setDescription(accountCode.getDescription());
		String category = accountCode.getCategory().getName();
		
		// check if account code category is a type debit
		// set the amount on debit entry
		if (Account.checkEntryType(category).equals(AccountConstants.ACC_ENTRY_DEBIT)) {
			jl.setDebit(totalLineAmount);
		}else if (Account.checkEntryType(category).equals(AccountConstants.ACC_ENTRY_CREDIT)) {
			// check if account code category is a type credit
			// set the amount on credit entry
			jl.setCredit(totalLineAmount);
		}
		
		return jl;
	}
}
