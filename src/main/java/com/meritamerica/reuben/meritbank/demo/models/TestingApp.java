package com.meritamerica.reuben.meritbank.demo.models;

public class TestingApp {
	public static void main(String arg[]) {
		CheckingAccount ch1 = new CheckingAccount(500);
		SavingsAccount sv1 = new SavingsAccount(499);
		
		AccountHolder ac1 = new AccountHolder("Reuben", "E","Pena", "233444555");
		
		MeritBank.addAccountHolder(ac1);
		
		ac1.addCheckingAccount(ch1);
		ac1.addSavingsAccount(sv1);
		
		System.out.println(ac1.checking[0]);
		
		CDAccount cda1 = new CDAccount(1000, 0.025, 5);
		
		ac1.addCDAccount(cda1);
		
		System.out.println(ac1.cdAccount[0]);
	}
}
