package com.meritamerica.reuben.meritbank.demo.models;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CDAccount extends BankAccount{
	//public CDOffering offering = new CDOffering(1, 0.01);
	
	private int term;
	
	// used for creation thru the REST API
	public CDAccount(){
        super(0.00, 0.01);
        this.term = 0;
    }
	
    public CDAccount(double balance, double interestRate, int term) {
    	//super(balance, interestRate);
    	super(balance, interestRate);
    }

    public CDAccount(CDOffering offering, double openingBalance){
        super(openingBalance, offering.getInterestRate());
        this.term = offering.getTerm();
    }
    
    public int getTerm(){
        return this.term;
    }
    
    public void setTerm(int term) {
    	this.term = term;
    }
    
    // override from BankAccount withdraw , deposit , futureValue
    // no need for withdraw for CDAccount
    public boolean withdraw(double amount){
        return false;
    }
	
	public boolean deposit(double amount) {
		// Desosit only allowed during creation
		if(this.getBalance() == 0) {
			try {
				if(amount <= 0){
		        	throw new NegativeAmountException("Please Deposit a positive amount.");
		        }
			} catch (NegativeAmountException e) {
				System.out.println(e);
				return false;
			}
	    
	        try {
	        	if(amount > 1000) {
		        	throw new ExceedsFraudSuspicionLimitException("Transaction requires review, thanks for your patience.");
	        	}
	        } catch(ExceedsFraudSuspicionLimitException e) {
	        	java.util.Date fDate = new java.util.Date();
		        DepositTransaction newTrans = new DepositTransaction(-1, this.getAccountNumber(), amount, fDate);
		        MeritBank.processTransaction(newTrans);
	        	System.out.println(e);
	        	FraudQueue.addTransaction(newTrans);
	        	return true;
	        }
	        java.util.Date fDate = new java.util.Date();
	        DepositTransaction newTrans = new DepositTransaction(-1, this.getAccountNumber(), amount, fDate);
	        MeritBank.processTransaction(newTrans);
	        return true;
		}
        return false;
    }
	
	public double futureValue() {
        double fv = MeritBank.recursiveFutureValue(this.getBalance(), this.getTerm(), this.getInterestRate());
        return fv;
	}
	
	public String toString(){
        DecimalFormat format = new DecimalFormat("##.00");
        return "\nCDAccount Balance: $" + format.format(this.getBalance()) + "\n"
                + "CDAccount Interest Rate: " + this.getInterestRate() + "\n"
                + "CDAccount Balance in 3 years: $" + format.format(this.futureValue(3));
    }
}
