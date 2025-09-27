package org.merra.entities;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.merra.audit.CreatedDate;
import org.merra.embedded.DefaultCurrency;
import org.merra.embedded.PhoneDetails;
import org.merra.entities.embedded.ExternalLinks;
import org.merra.entities.embedded.OrganizationNameUpdate;
import org.merra.entities.embedded.OrganizationUserInvites;
import org.merra.entities.embedded.OrganizationUsers;
import org.merra.enums.Status;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

@Entity
@Table(name = "organization", schema = "merra_schema")
public class Organization {

	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(name = "profile_image")
	@NotNull(message = "profile_image")
	private String profileImage;
	
	@ElementCollection
	@CollectionTable(
			schema = "merra_schema",
			name = "organization_name_log",
			joinColumns = {
					@JoinColumn(name = "organization_id", referencedColumnName = "id", nullable = false)
			})
	private Set<OrganizationNameUpdate> nameDetailsUpdate;
	
	@ElementCollection
	@CollectionTable(
			schema = "merra_schema",
			name = "organization_users",
			joinColumns = {@JoinColumn(name = "organization_id", referencedColumnName = "id", nullable = false)})
	private Set<OrganizationUsers> organizationUsers;
	
	@ElementCollection
	@CollectionTable(
			schema = "merra_schema",
			name = "org_invites",
			joinColumns = {@JoinColumn(name = " organization_id", referencedColumnName = "id", nullable = false)})
	private Set<OrganizationUserInvites> organizationUserInvites;
	
	@Column(name = "display_name", nullable = false, unique = true)
	@NotBlank(message = "displayName attribute cannot be blank.")
	private String displayName;
	
	// The official legal name or trading name of the business
	@Column(name = "legal_name", nullable = false, unique = true)
	@NotBlank(message = "legalName cannot be blank.")
	private String legalName;
	
	@Column(name = "organization_description")
	private String organizationDescription;
	
	// e.g., "US", "AU", "NZ", "GB"
	@Column(nullable = false)
	@NotNull(message = "country attribute cannot be null.")
	private String country;
	
	@Embedded
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "default_currency", nullable = false, columnDefinition = "jsonb")
	@NotNull(message = "defaultCurrency attribute cannot be null.")
	private DefaultCurrency defaultCurrency;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "organization_type", nullable = false)
	@NotNull(message = "organizationType attribute cannot be null.")
	private OrganizationType organizationType;
	
	/* Contact Details */
	@JdbcTypeCode(SqlTypes.JSON_ARRAY)
	@Column(name = "phone_no", nullable = false, columnDefinition = "jsonb[]")
	private LinkedHashSet<PhoneDetails> phoneNo;
	
	@Column(name = "email", nullable = false)
	@NotBlank(message = "Email is mandatory")
	@Email(message = "Email attribute should be valid")
	private String email;
	
	private String website;
	
//	@OneToOne(fetch = FetchType.LAZY, mappedBy = "organization")
//	private TaxDetails taxDetails;
	
	@NotBlank(message = "timeZone attribute is required.")
	@Column(name = "time_zone")
	private String timeZone;
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "financial_year", columnDefinition = "jsonb")
	private Map<String, Integer> financialYear;
	
	@JdbcTypeCode(SqlTypes.JSON_ARRAY)
	@Column(name = "address", nullable = false, columnDefinition = "jsonb[]")
	@NotNull(message = "Address attribute cannot be null.")
	private Set<Map<String, String>> address;
	
	@JdbcTypeCode(SqlTypes.JSON_ARRAY)
	@Column(name = "external_links", columnDefinition = "jsonb[]")
	private Set<ExternalLinks> externalLinks;
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "payment_terms")
	@NotNull(message = "paymentTerms attribute cannot be null.")
	private Map<String, String> paymentTerms;
	
	@Embedded
	@AttributeOverride(name = "createdDate", column = @Column(name = "created_date", nullable = false))
	private CreatedDate createdDate;
	
	// By default when an organization is created, it has an active subscription
	@Column(name = "active_subscription", nullable = false)
	private Boolean activeSubscription = true;
	
	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status = Status.ACTIVE_ACCOUNT;
	
	public void setStatus(Status status) {
		if (status != null) {
			this.status = status;
		} else {
			this.status = Status.ACTIVE_ACCOUNT;
		}
	}
	
	public void setActiveSubscription(Boolean isActiveSubscription) {
		this.activeSubscription = isActiveSubscription == null ?
				true : isActiveSubscription;
	}
	
	public void setBasicInformation(
			String displayName,
			String legalName,
			OrganizationType organizationType,
			String description
	) {
		this.setDisplayName(displayName);
		this.setLegalName(legalName);
		this.setOrganizationType(organizationType);
		this.setOrganizationDescription(description);
	}
	
	public void setContactDetails(
			String countryCode,
			DefaultCurrency defaultCurrency,
			Set<Map<String, String>> address,
			LinkedHashSet<PhoneDetails> phones,
			String email,
			String website,
			Set<ExternalLinks> externalLinks
	) {
		this.setCountry(countryCode);
		this.setDefaultCurrency(defaultCurrency);
		this.setAddress(address);
		this.setPhoneNo(phones);
		this.setEmail(email);
		this.setWebsite(website);
		this.setExternalLinks(externalLinks);
	}

	public Organization() {
	}

	public Organization(UUID id, @NotNull(message = "profile_image") String profileImage,
			Set<OrganizationNameUpdate> nameDetailsUpdate, Set<OrganizationUsers> organizationUsers,
			Set<OrganizationUserInvites> organizationUserInvites,
			@NotBlank(message = "displayName attribute cannot be blank.") String displayName,
			@NotBlank(message = "legalName cannot be blank.") String legalName, String organizationDescription,
			@NotNull(message = "country attribute cannot be null.") String country,
			@NotNull(message = "defaultCurrency attribute cannot be null.") DefaultCurrency defaultCurrency,
			@NotNull(message = "organizationType attribute cannot be null.") OrganizationType organizationType,
			LinkedHashSet<PhoneDetails> phoneNo,
			@NotBlank(message = "Email is mandatory") @Email(message = "Email attribute should be valid") String email,
			String website, @NotBlank(message = "timeZone attribute is required.") String timeZone,
			Map<String, Integer> financialYear,
			@NotNull(message = "Address attribute cannot be null.") Set<Map<String, String>> address,
			Set<ExternalLinks> externalLinks,
			@NotNull(message = "paymentTerms attribute cannot be null.") Map<String, String> paymentTerms,
			CreatedDate createdDate, Boolean activeSubscription, Status status) {
		this(profileImage, nameDetailsUpdate, organizationUsers, organizationUserInvites, displayName, legalName,
				organizationDescription, country, defaultCurrency, organizationType, phoneNo, email, website, timeZone,
				financialYear, address, externalLinks, paymentTerms, createdDate, activeSubscription, status);
		this.id = id;
	}

	public Organization(@NotNull(message = "profile_image") String profileImage,
			Set<OrganizationNameUpdate> nameDetailsUpdate, Set<OrganizationUsers> organizationUsers,
			Set<OrganizationUserInvites> organizationUserInvites,
			@NotBlank(message = "displayName attribute cannot be blank.") String displayName,
			@NotBlank(message = "legalName cannot be blank.") String legalName, String organizationDescription,
			@NotNull(message = "country attribute cannot be null.") String country,
			@NotNull(message = "defaultCurrency attribute cannot be null.") DefaultCurrency defaultCurrency,
			@NotNull(message = "organizationType attribute cannot be null.") OrganizationType organizationType,
			LinkedHashSet<PhoneDetails> phoneNo,
			@NotBlank(message = "Email is mandatory") @Email(message = "Email attribute should be valid") String email,
			String website, @NotBlank(message = "timeZone attribute is required.") String timeZone,
			Map<String, Integer> financialYear,
			@NotNull(message = "Address attribute cannot be null.") Set<Map<String, String>> address,
			Set<ExternalLinks> externalLinks,
			@NotNull(message = "paymentTerms attribute cannot be null.") Map<String, String> paymentTerms,
			CreatedDate createdDate, Boolean activeSubscription, Status status) {
		this.profileImage = profileImage;
		this.nameDetailsUpdate = nameDetailsUpdate;
		this.organizationUsers = organizationUsers;
		this.organizationUserInvites = organizationUserInvites;
		this.displayName = displayName;
		this.legalName = legalName;
		this.organizationDescription = organizationDescription;
		this.country = country;
		this.defaultCurrency = defaultCurrency;
		this.organizationType = organizationType;
		this.phoneNo = phoneNo;
		this.email = email;
		this.website = website;
		this.timeZone = timeZone;
		this.financialYear = financialYear;
		this.address = address;
		this.externalLinks = externalLinks;
		this.paymentTerms = paymentTerms;
		this.createdDate = createdDate;
		this.activeSubscription = activeSubscription;
		this.status = status;
	}

	public UUID getId() {
		return id;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public Set<OrganizationNameUpdate> getNameDetailsUpdate() {
		return nameDetailsUpdate;
	}

	public void setNameDetailsUpdate(Set<OrganizationNameUpdate> nameDetailsUpdate) {
		this.nameDetailsUpdate = nameDetailsUpdate;
	}

	public Set<OrganizationUsers> getOrganizationUsers() {
		return organizationUsers;
	}

	public void setOrganizationUsers(Set<OrganizationUsers> organizationUsers) {
		this.organizationUsers = organizationUsers;
	}

	public Set<OrganizationUserInvites> getOrganizationUserInvites() {
		return organizationUserInvites;
	}

	public void setOrganizationUserInvites(Set<OrganizationUserInvites> organizationUserInvites) {
		this.organizationUserInvites = organizationUserInvites;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getLegalName() {
		return legalName;
	}

	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}

	public String getOrganizationDescription() {
		return organizationDescription;
	}

	public void setOrganizationDescription(String organizationDescription) {
		this.organizationDescription = organizationDescription;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public DefaultCurrency getDefaultCurrency() {
		return defaultCurrency;
	}

	public void setDefaultCurrency(DefaultCurrency defaultCurrency) {
		this.defaultCurrency = defaultCurrency;
	}

	public OrganizationType getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(OrganizationType organizationType) {
		this.organizationType = organizationType;
	}

	public LinkedHashSet<PhoneDetails> getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(LinkedHashSet<PhoneDetails> phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public Map<String, Integer> getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear(Map<String, Integer> financialYear) {
		this.financialYear = financialYear;
	}

	public Set<Map<String, String>> getAddress() {
		return address;
	}

	public void setAddress(Set<Map<String, String>> address) {
		this.address = address;
	}

	public Set<ExternalLinks> getExternalLinks() {
		return externalLinks;
	}

	public void setExternalLinks(Set<ExternalLinks> externalLinks) {
		this.externalLinks = externalLinks;
	}

	public Map<String, String> getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(Map<String, String> paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public CreatedDate getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(CreatedDate createdDate) {
		this.createdDate = createdDate;
	}

	public Boolean getActiveSubscription() {
		return activeSubscription;
	}

	public Status getStatus() {
		return status;
	}

	
}
