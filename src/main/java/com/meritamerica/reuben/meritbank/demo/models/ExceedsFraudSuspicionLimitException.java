package com.meritamerica.reuben.meritbank.demo.models;

public class ExceedsFraudSuspicionLimitException extends Exception {
	public ExceedsFraudSuspicionLimitException(String message) {
		super(message);
	}
}
