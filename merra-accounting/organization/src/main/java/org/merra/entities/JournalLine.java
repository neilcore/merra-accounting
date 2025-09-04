package org.merra.entities;

import java.math.BigDecimal;
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
	
	@Column(columnDefinition = "numeric(7,2)")
	private BigDecimal credit;
	
	@Column(columnDefinition = "numeric(7,2)")
	private BigDecimal debit;
}
