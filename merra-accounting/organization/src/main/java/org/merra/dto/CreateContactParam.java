package org.merra.dto;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.merra.embedded.PhoneDetails;

import jakarta.validation.constraints.NotBlank;

public record CreateContactParam (
		Name name,
		ContactDetail contactDetails,
		String taxNumber,
		UUID paymentTerms
) {
	
	public record Name(
			@NotBlank(message="name component cannot be null.")
			String name,
			String firstName,
			String lastName,
			Boolean isSupplier,
			Boolean isCustomer
	) {}
	
	public record ContactDetail(
			String emailAddress,
			String companyNumber,
			Set<Map<String, String>> address,
			LinkedHashSet<PhoneDetails> phone
	) {}
}