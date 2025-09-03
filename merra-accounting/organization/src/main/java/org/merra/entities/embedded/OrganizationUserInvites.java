package org.merra.entities.embedded;

import java.time.LocalDate;

import org.merra.entities.UserAccount;
import org.merra.utilities.OrganizationConstants;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationUserInvites {
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "invitation_for", nullable = false)
	@NotNull(message = "InvitationFor field cannot be null.")
	private UserAccount invitationFor;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "invitation_by", nullable = false)
	@NotNull(message = "InvitationBy field cannot be null.")
	private UserAccount invitationBy;
	
	@Column(name = "invitation_role", nullable = false)
	@NotBlank(message = "invitationRole field cannot be null")
	private String invitationRole;
	
	@Column(name = "invite_date", nullable = false)
	@DateTimeFormat(iso = ISO.DATE)
	@Builder.Default
	private LocalDate inviteDate = LocalDate.now();
	
	@Column(name = "invitation_status", nullable = false)
	@Builder.Default
	private String invitationStatus = OrganizationConstants.INVITATION_STATUS_PENDING;
}