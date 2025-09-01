package org.merra.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.merra.entities.Invoice;
import org.merra.entities.Journal;
import org.merra.entities.JournalLine;
import org.merra.entities.LineItem;
import org.merra.entities.Organization;
import org.merra.entities.embedded.AccountDetail;
import org.merra.entities.embedded.TaxDetail;
import org.merra.repositories.AccountRepository;
import org.merra.repositories.InvoiceRepository;
import org.merra.repositories.JournalRepository;
import org.merra.repositories.projections.JournalAccountLookup;
import org.springframework.stereotype.Service;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JournalService {
	private final JournalRepository journalRepository;
	private final AccountRepository accountRepository;
	
	private JournalLine createJournalLineEntry(
			Journal journal,
			Map<String, BigDecimal> amounts,
			@NotNull JournalAccountLookup journalAccount
	) {
		AccountDetail accountDetail = new AccountDetail(
				journalAccount.getAccountId(),
				journalAccount.getAccountCode(),
				journalAccount.getAccountType().getName(),
				journalAccount.getAccountName()
		);
		// TODO - tax details for journal
		TaxDetail taxDetail = new TaxDetail(
		);
		return JournalLine.builder()
				.journal(journal)
				.accountDetails(accountDetail)
				.description(journalAccount.getDescription())
				.grossAmount(amounts.get("gross"))
				.netAmount(amounts.get("net"))
				.taxDetail(taxDetail)
				.build();
	}
	public void entry(Set<LineItem> lineItems, Organization org, Invoice findInvoiceById) {
		Optional<JournalAccountLookup> getAccount = Optional.empty();
		
		Journal createJournal = new Journal();
		createJournal.setJournalDate(findInvoiceById.getDate());
		
		String EXCLUSIVE_TYPE = InvoiceRepository.INVOICE_LINE_AMOUNT_TYPE_INCLUSIVE;
		Map<String, BigDecimal> amounts = Map.of("gross", null, "net", null, "totalTax", findInvoiceById.getTotalTax());
		// If invoice is tax exclusive
		if(findInvoiceById.getLineAmountTypes().equals(EXCLUSIVE_TYPE)) {
			amounts.putIfAbsent("gross", findInvoiceById.getGrandTotal());
			amounts.putIfAbsent("net", findInvoiceById.getGrandTotal());
		}
		
		/**
		 * Get the accounts receivable or debit account
		 * if it is a customer invoice
		 */
		String CUSTOMER_INVOICE = InvoiceRepository.INVOICE_TYPE_CUSTOMER_INVOICE;
		JournalLine debitJournalLine = null;
		if (findInvoiceById.getType().equalsIgnoreCase(CUSTOMER_INVOICE)) {
			Optional<JournalAccountLookup> getDebitAccount = accountRepository
					.findJournalAccountDetail(AccountRepository.ACC_CODE_ACC_RECEIVABLE, org.getId());
			debitJournalLine = 
					createJournalLineEntry(createJournal, amounts, getDebitAccount.get());
		}
		
		List<JournalLine> journalLines = new ArrayList<>();
		if (debitJournalLine != null) {
			journalLines.add(debitJournalLine);
		}
		
		for (LineItem lt: lineItems) {
			getAccount = accountRepository.findJournalAccountDetail(lt.getAccountCode(),org.getId());
			journalLines.add(createJournalLineEntry(createJournal, amounts, getAccount.get()));
		}
	}
}
