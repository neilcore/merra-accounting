package org.merra.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "ContactPaymentTerms")
@Table(name = "payment_terms", schema = "merra_schema")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentTerms {
	
	@Id @GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "payment_term_id", nullable = false, unique = true)
	private UUID id;
	
	@Column(name = "label", nullable = false, unique = true)
	@NotNull(message = "label cannot be null.")
	private String label;
	
	private String description;
}
