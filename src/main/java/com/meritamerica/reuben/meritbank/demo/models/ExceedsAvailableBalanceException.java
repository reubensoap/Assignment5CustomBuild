package com.meritamerica.reuben.meritbank.demo.models;

public class ExceedsAvailableBalanceException extends Exception {
	public ExceedsAvailableBalanceException(String message) {
		super(message);
	}
}
