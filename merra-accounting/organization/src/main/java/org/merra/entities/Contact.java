package org.merra.entities;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;
import org.hibernate.type.SqlTypes;
import org.merra.embedded.PhoneDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * field @name is the only non optional attribute in this entity.
 */
@Entity(name = "Contact")
@Table(name = "contacts", schema = "merra_schema")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contact {

	@Id @GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "contact_id", nullable = false, unique = true)
	private UUID id;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "organization", nullable = false, referencedColumnName = "id")
	private Organization organization;
	
	// Full name of contact / organization
	@Column(name = "name", nullable = false, unique = true)
	@NotBlank(message = "Name cannot be blank")
	private String name;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "email_address")
	@Email(message = "Email address must be valid")
	private String emailAddress;
	
	@JdbcTypeCode(SqlTypes.JSON_ARRAY)
	@Column(name = "phone_no", nullable = false, columnDefinition = "jsonb[]")
	private LinkedHashSet<PhoneDetails> phoneNo;
	
	@Column(name = "account_number")
	private String accountNumber;
	
	// Company registration number
	@Column(name = "company_number")
	private String companyNumber;
	
	@Column(name = "default_discount")
	private Integer defaultDiscount;
	
	// Tax number
	@Column(name = "tax_number")
	private String taxNumber;
	
	@Column(name = "contact_status", nullable = false)
	@Builder.Default
	private String contactStatus = "ACTIVE";
	
	@Column(name = "is_supplier", nullable = false)
	@NotNull(message = "Supplier status cannot be null")
	private Boolean isSupplier;
	
	@Column(name = "is_customer", nullable = false)
	@NotNull(message = "Customer status cannot be null")
	private Boolean isCustomer;
	
	
	// Only shown in GET response, not in POST/PUT
	@JdbcTypeCode(SqlTypes.JSON_ARRAY)
	@Column(name = "address", columnDefinition = "jsonb")
	private Map<String, Object> address;
	
	// Returned in GET response, not in POST/PUT
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "payment_terms", referencedColumnName = "payment_term_id")
	private PaymentTerms paymentTerms;
	
	// Returned in GET response, not in POST/PUT
	@Column(name = "updated_date_utc")
	@TimeZoneStorage(TimeZoneStorageType.NORMALIZE_UTC)
	private Instant updatedDateUTC;
	
	public Contact(@NotBlank String name) {
		this.name = name;
	}
	
	public void setIsSupplier(Boolean sup) {
		this.isSupplier = sup ? Boolean.TRUE : Boolean.FALSE;
	}
	
	public void setIsCustomer(Boolean cus) {
		this.isCustomer = cus ? Boolean.TRUE : Boolean.FALSE;
	}
}
