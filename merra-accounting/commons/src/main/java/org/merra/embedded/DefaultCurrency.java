package org.merra.embedded;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
public class DefaultCurrency implements Serializable {
	@NotNull(message = "name cannot be null.")
	private String name;
	
	@NotNull(message = "symbol cannot be null.")
	private String symbol;
	
	@NotNull(message = "currencyCode cannot be null.")
	private String currencyCode;

	public DefaultCurrency() {
	}

	public DefaultCurrency(@NotNull(message = "name cannot be null.") String name,
			@NotNull(message = "symbol cannot be null.") String symbol,
			@NotNull(message = "currencyCode cannot be null.") String currencyCode) {
		this.name = name;
		this.symbol = symbol;
		this.currencyCode = currencyCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	
}