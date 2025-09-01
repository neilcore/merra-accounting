package org.merra.embedded;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class DefaultCurrency implements Serializable {
	@NotNull(message = "name cannot be null.")
	private String name;
	
	@NotNull(message = "symbol cannot be null.")
	private String symbol;
	
	@NotNull(message = "currencyCode cannot be null.")
	private String currencyCode;
}