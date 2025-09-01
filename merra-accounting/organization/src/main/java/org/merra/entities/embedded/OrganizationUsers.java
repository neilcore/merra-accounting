package org.merra.entities.embedded;

import java.time.LocalDate;

import org.merra.entities.UserAccount;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationUsers {	
	@ManyToOne(targetEntity = UserAccount.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private UserAccount userId;
	
	@NotBlank(message = "User role cannot be blank")
	@Column(name = "user_role", nullable = false)
	private String userRole;
	
	@Column(name = "user_joined", nullable = false)
	@Builder.Default
	private LocalDate userJoined = LocalDate.now();
	
	@Column(name = "organization_status", nullable = false)
	@NotBlank(message = "organizationStatus cannpt be blank.")
	private String organizationStatus;
}

