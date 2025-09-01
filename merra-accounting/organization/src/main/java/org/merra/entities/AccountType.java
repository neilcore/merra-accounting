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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "account_type", schema = "merra_schema")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountType {
	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(unique = true, nullable = false)
	@NotNull(message = "name cannot be null.")
	private String name;
	private String description;

	
	public AccountType(@NotNull String name, String description) {
		this.name = name;
		this.description = description;
	}
}