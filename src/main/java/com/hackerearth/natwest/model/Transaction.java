package com.hackerearth.natwest.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class Transaction implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull(message = "Account Number cannot be null")
	private String accountNumber;

	@NotNull(message = "Type cannot be null")
	private String type;

	@NotNull(message = "Amount cannot be null")
	private Long amount;

	@NotNull(message = "Currency cannot be null")
	private String currency;

	@NotNull(message = "Account From cannot be null")
	private String accountFrom;

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAccountFrom() {
		return accountFrom;
	}

	public void setAccountFrom(String accountFrom) {
		this.accountFrom = accountFrom;
	}

}
