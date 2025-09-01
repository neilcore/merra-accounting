package org.merra.entities.embedded;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountDetail implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull(message = "accountId attribute cannot be null.")
	private UUID accountId;
	
	@NotBlank(message = "accountCode attribute cannot be blank.")
	private String accountCode;
	
	@NotBlank(message = "accountType attribute cannot be blank.")
	private String accountType;
	
	private String accountName;
}
