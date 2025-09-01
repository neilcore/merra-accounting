package org.merra.entities;

import java.math.BigDecimal;
import java.sql.SQLType;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.merra.entities.embedded.AccountDetail;
import org.merra.entities.embedded.TaxDetail;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "journal_line", schema = "merra_schema")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class JournalLine {
	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "journal", nullable = false, referencedColumnName = "journal_id")
	private Journal journal;
	
	@Embedded
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "account_details", nullable = false, columnDefinition = "jsonb")
	@NotNull(message = "accountDetails attribute cannot be null.")
	private AccountDetail accountDetails;
	
	private String description;
	
	@Column(name = "net_amount", nullable = false, columnDefinition = "numeric(7,2)")
	@NotNull(message = "netAmount attribute cannot be null.")
	private BigDecimal netAmount;
	
	// Gross amount of journal line (NetAmount + TaxAmount).
	@Column(name = "gross_amount", nullable = false, columnDefinition = "numeric(7,2)")
	@NotNull(message = "grossAmount attribute cannot be null.")
	private BigDecimal grossAmount;
	
	@Embedded
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "tax_detail", columnDefinition = "jsonb")
	private TaxDetail taxDetail;
	
	public JournalLine(
			Journal journal,
			AccountDetail accountDetails,
			String description,
			BigDecimal netAmount,
			BigDecimal grossAmount,
			TaxDetail taxDetail
	) {
		this.journal = journal;
		this.accountDetails = accountDetails;
		this.description = description;
		this.netAmount = netAmount;
		this.grossAmount = grossAmount;
		this.taxDetail = taxDetail;
	}
}
