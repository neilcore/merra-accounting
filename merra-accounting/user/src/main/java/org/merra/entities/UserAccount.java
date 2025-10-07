package org.merra.entities;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.merra.entities.embedded.UserTokens;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "user_account", schema = "merra_schema")
public class UserAccount implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "user_id", nullable = false, unique = true)
	private UUID userId;

	@Column(nullable = false, unique = true, name = "email")
	@Email(message = "Email should be valid")
	private String email;

	@Column(name = "first_name", nullable = false)
	@NotBlank(message = "First name is mandatory")
	private String firstName;

	@Column(name = "last_name", nullable = false)
	@NotBlank(message = "Last name is mandatory")
	private String lastName;

	@Column(name = "account_password", nullable = false)
	@NotNull(message = "accountPassword cannot be null.")
	private String accountPassword;

	@Column(name = "contact_number", columnDefinition = "jsonb")
	@JdbcTypeCode(SqlTypes.JSON)
	private Map<String, String> contactNumber;

	@Column(name = "account_role", nullable = false)
	@NotNull(message = "Roles cannot be null")
	private String roles = "NONE";

	// A user by default doesn't own any organization
	@Column(name = "is_owner", nullable = false)
	private boolean isOwner = false;

	@Column(nullable = false)
	@NotBlank(message = "country attribute cannot be blank.")
	private String country;

	// A user by default isn't part of any organization
	@Column(name = "part_of_organization", nullable = false)
	private boolean partOfOrganization = false;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "userAccount")
	private UserAccountSettings accountSettings;

	@Column(nullable = false, name = "is_enabled")
	private boolean isEnabled = false; // Default to enabled when account is created

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "tokens", columnDefinition = "jsonb")
	private UserTokens tokens;

	public UserTokens getTokens() {
		return tokens;
	}

	public void setTokens(UserTokens tokens) {
		this.tokens = tokens;
	}

	@Override
	public boolean isEnabled() {
		return this.isEnabled; // Use your entity field
	}

	public void setIsEnabled(boolean en) {
		this.isEnabled = en;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(roles));
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public String getPassword() {
		return this.accountPassword;
	}

	public UserAccount() {
	}

	public UserAccount(@Email(message = "Email should be valid") String email,
			@NotBlank(message = "First name is mandatory") String firstName,
			@NotBlank(message = "Last name is mandatory") String lastName,
			@NotNull(message = "accountPassword cannot be null.") String accountPassword,
			Map<String, String> contactNumber, @NotNull(message = "Roles cannot be null") String roles, boolean isOwner,
			boolean partOfOrganization, UserAccountSettings accountSettings) {
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.accountPassword = accountPassword;
		this.contactNumber = contactNumber;
		this.roles = roles;
		this.isOwner = isOwner;
		this.partOfOrganization = partOfOrganization;
		this.accountSettings = accountSettings;
	}

	public UUID getUserId() {
		return userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAccountPassword() {
		return accountPassword;
	}

	public void setAccountPassword(String accountPassword) {
		this.accountPassword = accountPassword;
	}

	public Map<String, String> getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(Map<String, String> contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public boolean isOwner() {
		return isOwner;
	}

	public void setOwner(boolean isOwner) {
		this.isOwner = isOwner;
	}

	public boolean isPartOfOrganization() {
		return partOfOrganization;
	}

	public void setPartOfOrganization(boolean partOfOrganization) {
		this.partOfOrganization = partOfOrganization;
	}

	public UserAccountSettings getAccountSettings() {
		return accountSettings;
	}

	public void setAccountSettings(UserAccountSettings accountSettings) {
		this.accountSettings = accountSettings;
	}

}
