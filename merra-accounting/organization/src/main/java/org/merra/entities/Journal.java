package org.merra.entities;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.merra.entities.embedded.JournalTotalAmountEntry;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * TODO - TO BE CONTINUED.
 */
@Entity
@Table(name = "journal", schema = "merra_schema")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Journal {
	
	@Id @GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "journal_id", nullable = false, unique = true)
	private UUID journalId;
	
	@Column(name = "journal_date", nullable = false)
	@NotNull(message = "journalDate attribute cannot be null.")
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate journalDate;
	
	/**
	 * this is system generated value
	 * only include in GET request.
	 */
	@Column(
			name = "journal_number",
			nullable = false,
			unique = true,
			updatable = false,
			columnDefinition = "BIGINT GENERATED ALWAYS AS IDENTITY UNIQUE"
	)
	private BigInteger journalNumber;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "journal")
	@NotEmpty(message = "journalLines attribute cannot be empty.")
	private List<JournalLine> journalLines;
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "total", columnDefinition = "jsonb")
	private JournalTotalAmountEntry total;
}
