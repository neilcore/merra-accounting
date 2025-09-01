package org.merra.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "organization_type", schema = "merra_schema")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrganizationType {
	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@NotBlank(message = "Name cannot be blank")
	@Column(name = "name", nullable = false, unique = true)
	private String name;
	
	public OrganizationType(String name) {
		this.name = name;
	}

}
