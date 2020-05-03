package com.meritamerica.reuben.meritbank.demo.models;

import java.util.*;

import javax.validation.constraints.NotBlank;

 public abstract class BankAccount {
	// Variables 
	@NotBlank(message = "Balance is mandatory")
	private double balance = 0;
	
	private double interestRate;
	private long accountNumber;
	private java.util.Date accountOpenedOn;
	
	List<Transaction> list = new ArrayList<Transaction> ();
	
	public BankAccount(double balance, double interestRate) {
		this.balance = balance;
		this.interestRate = interestRate;
		this.accountOpenedOn = new java.util.Date();
		// no transaction methods at this time
	}
	
	// used for creating Checking Accounts, Savings Accounts, CDAccounts
	public BankAccount(double balance, double interestRate
			, java.util.Date accountOpenedOn) {
		this.balance = balance;
		this.interestRate = interestRate;
		this.accountOpenedOn = accountOpenedOn;
		this.accountNumber = MeritBank.getNextAccountNumber();
		DepositTransaction newTrans = new DepositTransaction(-1, this.accountNumber, this.balance, this.accountOpenedOn);
		this.addTransaction(newTrans);
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}

	public long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public java.util.Date getAccountOpenedOn() {
		return accountOpenedOn;
	}

	public void setAccountOpenedOn(java.util.Date accountOpenedOn) {
		this.accountOpenedOn = accountOpenedOn;
	}

	public double futureValue(int years) {
        double fv = MeritBank.recursiveFutureValue(this.balance, 3, this.interestRate);
        return fv;
    }
	
	public boolean withdraw(double amount) {
		try {
			if((this.getBalance() - amount) < 0){      
	            throw new ExceedsAvailableBalanceException("Not enough funds in the account.");
	        }
		} catch(ExceedsAvailableBalanceException e) {
			System.out.println(e);
			return false;
		}
		
		try {
			if (Math.abs(amount) > 1000) {
        		throw new ExceedsFraudSuspicionLimitException("Transaction requires review, thanks for your patience.");
        	}
		} catch (ExceedsFraudSuspicionLimitException e) {
			java.util.Date fDate = new java.util.Date();
	        WithdrawTransaction newTrans = new WithdrawTransaction(-1, this.accountNumber, amount, fDate);
	        MeritBank.processTransaction(newTrans);
			System.out.println(e);
			FraudQueue.addTransaction(newTrans);
			return true;
		}
        
        java.util.Date fDate = new java.util.Date();
        WithdrawTransaction newTrans = new WithdrawTransaction(-1, this.accountNumber, amount, fDate);
        MeritBank.processTransaction(newTrans);
        return true;
    }
	
	/* Deposit method 
	 * 	amount required to be more than a negative
	 * 	if amount > 1000, processes transaction AND adds transaction to FQ
	 * 	if account is not under MeritBank stash, manually deposits money AND
	 *  adds transaction
	 */
	public boolean deposit(double amount) {
		
		
		try {
			if(amount < 0){
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
	        DepositTransaction newTrans = new DepositTransaction(-1, this.accountNumber, amount, fDate);
	        MeritBank.processTransaction(newTrans);
        	System.out.println(e);
        	FraudQueue.addTransaction(newTrans);
        	return true;
        }
        	
        java.util.Date fDate = new java.util.Date();
        DepositTransaction newTrans = new DepositTransaction(-1, this.accountNumber, amount, fDate);
        BankAccount tester = MeritBank.getBankAccount(this.accountNumber);
        if(tester == null) {
        	this.balance += amount;
        	this.addTransaction(newTrans);
        	return true;
        } else {
        	MeritBank.processTransaction(newTrans);
	        return true;
        }
    }
	
	/* Transfer method
	 * 	amount required to be more than negative
	 * 	transfer amount required to be less than balance
	 * 	if amount > 1000, processes transaction AND adds transaction to FQ
	 * 	transaction required to have target accountNum instead of -1
	 */	
	
	public boolean transfer(double amount, long target) {
		try {
			if(amount < 0) {
				throw new NegativeAmountException("Please Deposit a positive amount.");
			}
		} catch(NegativeAmountException e) {
			System.out.println(e);
			return false;
		}
		try {
			if(amount > this.balance) {
				throw new ExceedsAvailableBalanceException("Not enough funds in the account.");
			}
		} catch(ExceedsAvailableBalanceException e) {
			System.out.println(e);
			return false;
		}
		try {
        	if(amount > 1000) {
	        	throw new ExceedsFraudSuspicionLimitException("Transaction requires review, thanks for your patience.");
        	}
        } catch(ExceedsFraudSuspicionLimitException e) {
        	java.util.Date fDate = new java.util.Date();
			TransferTransaction newTrans = new TransferTransaction(target, this.accountNumber, amount, fDate);
	        MeritBank.processTransaction(newTrans);
        	System.out.println(e);
        	FraudQueue.addTransaction(newTrans);
        	return true;
        }
		
		java.util.Date fDate = new java.util.Date();
		TransferTransaction newTrans = new TransferTransaction(target, this.accountNumber, amount, fDate);
        MeritBank.processTransaction(newTrans);
        return true;
	}
	
	// transaction methods
	
	public void addTransaction(Transaction transaction) {
		this.list.add(transaction);
	}
	
	public List<Transaction> getTransaction(){
		return this.list;
	}
	
	
}
