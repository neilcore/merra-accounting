package org.merra.entities;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import org.merra.audit.CreatedDate;
import org.merra.repositories.TaxRateRepository;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tax_rate", schema = "merra_schema")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaxRate {
	
	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "organization", nullable = false, referencedColumnName = "id")
	private Organization organization;
	
	@Column(nullable = false, unique = true)
	@NotBlank(message = "name attribute cannot be null.")
	private String name;
	
	@Column(name = "system_defined_name", nullable = false)
	@NotBlank(message = "systemDefinedName attribute cannot be blank.")
	private String systemDefinedName;
	
	@Column(name = "the_type", nullable = false, unique = true)
	@NotBlank(message = "taxType attribute cannot be blank.")
	private String taxType;
	
	@OneToMany(mappedBy = "taxRate", fetch = FetchType.EAGER)
	private Set<TaxComponent> taxComponent;
	
	// Shown for display purposes.
	// Presents the tax rate to the end-user in a clear and understandable format.
	@Column(name = "tax_rate_display", scale = 4, precision = 7)
	private BigDecimal taxRateDisplay;
	
	// The actual tax rate used for calculating the tax amount on transactions.
	@Column(name = "effective_rate", scale = 4, precision = 7)
	private BigDecimal effectiveRate;
	
	@Column(nullable = false)
	@NotBlank(message = "status attribute cannot be null.")
	private String status;
	
	@Column(name = "apply_to_asset_account")
	private Boolean applyToAssetAccount;
	
	@Column(name = "apply_to_equity_account")
	private Boolean applyToEquityAccount;
	
	@Column(name = "apply_to_expenses_account")
	private Boolean applyToExpensesAccount;
	
	@Column(name = "apply_to_liabilities_account")
	private Boolean applyToLiabilitiesAccount;
	
	@Column(name = "apply_to_revenue_account")
	private Boolean applyToRevenueAccount;
	
	@Embedded
	@AttributeOverride(name = "createdDate", column = @Column(name = "created_date", nullable = false))
	private CreatedDate createdDate;
	
	public void setStatus(String stat) {
		Set<String> taxStatuses = Set.of(
				TaxRateRepository.TAXRATE_STATUS_ACTIVE,
				TaxRateRepository.TAXRATE_STATUS_ARCHIVED,
				TaxRateRepository.TAXRATE_STATUS_DELETED
		);
		if (!taxStatuses.contains(stat.toUpperCase())) {
			throw new NoSuchElementException("Tax status value unrecognised.");
		}else {
			this.status = stat.toUpperCase();
		}
	}
	
	public void setApplyToAssetAccount(Boolean asset) {
		this.applyToAssetAccount = asset == null ? false : asset;
	}
	
	public void setApplyToEquityAccount(Boolean equity) {
		this.applyToEquityAccount = equity == null ? false : equity;
	}
	
	public void setApplyToExpensesAccount(Boolean expense) {
		this.applyToExpensesAccount = expense == null ? false : expense;
	}
	
	public void setApplyToLiabilitiesAccount(Boolean liability) {
		this.applyToLiabilitiesAccount = liability == null ? false : liability;
	}
	
	public void setApplyToRevenueAccount(Boolean revenue) {
		this.applyToRevenueAccount = revenue == null ? false : revenue;
	}
}
