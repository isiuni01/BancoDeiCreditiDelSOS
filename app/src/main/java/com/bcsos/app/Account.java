package com.bcsos.app;

import java.io.Serializable;
import java.util.UUID;

public class Account implements Serializable {

	private static final long serialVersionUID = 1L;
	private UUID id;
	private String name;
	private String surname;
	private double balance;
	
	
	public Account(String name, String surname) {
		this.id = UUID.randomUUID();
		this.name = name;
		this.surname = surname;
		this.balance = 0;
	}
	
	public void transfer(double amount) throws BalanceException {
		
		if(amount < 0) {
			if(balance < Math.abs(amount))
				throw new BalanceException("your balance is not sufficent");
			this.balance = this.balance + amount;
		} else {
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
	
	public UUID getId() {
		return id;
	}

	public double getBalance() {
		return balance;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", name=" + name + ", surname=" + surname + ", balance=" + balance + "]";
	}	
}
