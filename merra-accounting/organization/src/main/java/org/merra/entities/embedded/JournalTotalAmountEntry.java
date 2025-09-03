package org.merra.entities.embedded;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JournalTotalAmountEntry implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal totalDebit;
	private BigDecimal totalCredit;

}
