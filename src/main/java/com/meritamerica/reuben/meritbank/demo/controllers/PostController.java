package com.meritamerica.reuben.meritbank.demo.controllers;

import java.util.ArrayList;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.meritamerica.reuben.meritbank.demo.models.AccountHolder;
import com.meritamerica.reuben.meritbank.demo.models.BankAccount;
import com.meritamerica.reuben.meritbank.demo.models.CDAccount;
import com.meritamerica.reuben.meritbank.demo.models.CDOffering;
import com.meritamerica.reuben.meritbank.demo.models.CheckingAccount;
import com.meritamerica.reuben.meritbank.demo.models.ExceedsCombinedBalanceLimitException;
import com.meritamerica.reuben.meritbank.demo.models.MeritBank;
import com.meritamerica.reuben.meritbank.demo.models.NegativeAmountException;
import com.meritamerica.reuben.meritbank.demo.models.NoSuchResourceFoundException;
import com.meritamerica.reuben.meritbank.demo.models.SavingsAccount;

@RestController
public class PostController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String test() {
		return "Hello world";
	}

	@GetMapping(value = "/AccountHolders")
	public AccountHolder[] getAccountHolders(){
		return MeritBank.getAccountHolders();
	}
	
	@PostMapping(value = "/AccountHolders")
	@ResponseStatus(HttpStatus.CREATED)
	public AccountHolder addAccountHolder(@RequestBody @Valid AccountHolder account) {
		MeritBank.addAccountHolder(account);
		return account;
	}
	
	@GetMapping(value = "/AccountHolders/{id}")
	public AccountHolder getAccountHolderById(@PathVariable long id) throws NoSuchResourceFoundException {
		if(id > MeritBank.getAccountHolders().length - 1) {
			throw new NoSuchResourceFoundException("Invalid id");
		}
		return MeritBank.getAccountHolder(id);
	}
	
	// Checking Account Methods
	
	@GetMapping(value = "/AccountHolders/{id}/CheckingAccounts")
	public CheckingAccount[] getCheckingAccounts(@PathVariable long id) throws NoSuchResourceFoundException {
		if(id > MeritBank.getAccountHolders().length - 1) {
			throw new NoSuchResourceFoundException("Invalid id");
		}
		AccountHolder aHolder = MeritBank.getAccountHolder(id);
		return aHolder.getCheckingAccounts();
	}
	
	@PostMapping(value = "/AccountHolders/{id}/CheckingAccounts")
	@ResponseStatus(HttpStatus.CREATED)
	public CheckingAccount addCheckingAccount(@PathVariable long id, @RequestBody CheckingAccount account) throws NoSuchResourceFoundException, ExceedsCombinedBalanceLimitException, NegativeAmountException {
		if(id > MeritBank.getAccountHolders().length - 1) {
			throw new NoSuchResourceFoundException("Invalid id");
		}
		AccountHolder aHolder = MeritBank.getAccountHolder(id);
		double combinedBalance = aHolder.getCombinedBalance();
		if((combinedBalance + account.getBalance()) > 250000) {
			throw new ExceedsCombinedBalanceLimitException("Exceeds account limit");
		} else if (account.getBalance() < 0){
			throw new NegativeAmountException("Balance below 0");
		} else {
			account.setAccountNumber(MeritBank.getNextAccountNumber());
			aHolder.addCheckingAccount(account);
		}
		return account;
	}
	
	// Saving Account Methods
	
	@GetMapping(value = "/AccountHolders/{id}/SavingsAccounts")
	public SavingsAccount[] getSavingsAccounts(@PathVariable long id) throws NoSuchResourceFoundException {
		if(id > MeritBank.getAccountHolders().length - 1) {
			throw new NoSuchResourceFoundException("Invalid id");
		}
		AccountHolder aHolder = MeritBank.getAccountHolder(id);
		return aHolder.getSavingsAccounts();
	}
	
	@PostMapping(value = "/AccountHolders/{id}/SavingsAccounts")
	@ResponseStatus(HttpStatus.CREATED)
	public SavingsAccount addSavingsAccount(@PathVariable long id, @RequestBody SavingsAccount account) throws NoSuchResourceFoundException, ExceedsCombinedBalanceLimitException, NegativeAmountException {
		if(id > MeritBank.getAccountHolders().length - 1) {
			throw new NoSuchResourceFoundException("Invalid id");
		}
		AccountHolder aHolder = MeritBank.getAccountHolder(id);
		double combinedBalance = aHolder.getCombinedBalance();
		if((combinedBalance + account.getBalance()) > 250000) {
			throw new ExceedsCombinedBalanceLimitException("Exceeds account limit");
		} else if (account.getBalance() < 0) {
			throw new NegativeAmountException("Balance below 0");
		} else {
			account.setAccountNumber(MeritBank.getNextAccountNumber());
			aHolder.addSavingsAccount(account);
		}
		return account;
	}
	
	// CDAccount Methods
	
	@GetMapping(value = "/AccountHolders/{id}/CDAccounts")
	public CDAccount[] getCDAccounts(@PathVariable long id) throws NoSuchResourceFoundException {
		if(id > MeritBank.getAccountHolders().length - 1) {
			throw new NoSuchResourceFoundException("Invalid id");
		}
		AccountHolder aHolder = MeritBank.getAccountHolder(id);
		return aHolder.getCDAccounts();
	}
	
	@PostMapping(value = "/AccountHolders/{id}/CDAccounts")
	public CDAccount addCDAccounts(@PathVariable long id, @RequestBody CDAccount account) throws NoSuchResourceFoundException, ExceedsCombinedBalanceLimitException, NegativeAmountException {
		if(id > MeritBank.getAccountHolders().length - 1) {
			throw new NoSuchResourceFoundException("Invalid id");
		}
		AccountHolder aHolder = MeritBank.getAccountHolder(id);
		double combinedBalance = aHolder.getCombinedBalance();
		if((combinedBalance + account.getBalance()) > 250000) {
			throw new ExceedsCombinedBalanceLimitException("Exceeds account limit");
		} else if (account.getBalance() < 0) {
			throw new NegativeAmountException("Balance below 0");
		} else {
			account.setAccountNumber(MeritBank.getNextAccountNumber());
			aHolder.addCDAccount(account);
		}
		return account;
	}
	
	// CDOffering Methods
	
	@GetMapping(value = "/CDOfferings")
	public CDOffering[] getCDOfferings() {
		return MeritBank.getCDOfferings();
	}
	
	@PostMapping(value = "/CDOfferings")
	public CDOffering addCDOffering(@RequestBody CDOffering account) {
		CDOffering cdHolder[] = new CDOffering[MeritBank.getCDOfferings().length + 1];
		CDOffering copyHolder[] = MeritBank.getCDOfferings();
        for(int x = 0; x < MeritBank.getCDOfferings().length; x++) {
        	cdHolder[x] = copyHolder[x];
        }
        cdHolder[MeritBank.getCDOfferings().length - 1] = account;
        MeritBank.setCDOfferings(cdHolder);
        return account;
	}
}
