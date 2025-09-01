package org.merra.entities.embedded;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaxDetail implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Double taxAmount;
	private String taxType;
	private String taxName;
}
