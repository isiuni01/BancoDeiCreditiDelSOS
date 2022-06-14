package com.bcsos.app;

public class Account {

	
	private String name;
	private String surname;
	private double balance;
	
	
	public Account(String name, String surname) {
		this.name = name;
		this.surname = surname;
		this.balance = 0;
	}
	
	public void transfer(double amount) throws BalanceException {
		
		if(amount < 0)
		{
			if(balance < Math.abs(amount))
			{
				throw new BalanceException();
			}
			
			this.balance = this.balance + amount;
		}else {
			
			this.balance = this.balance + amount;
			
		}
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	
	
	
	
	
	
	
	
	
}