package com.bcsos.app;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Bank implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private ConcurrentHashMap<UUID, Account> mappa;
	private ConcurrentHashMap<UUID, Transaction> frasco;
	
	public Bank()
	{
		this.mappa = new ConcurrentHashMap<UUID, Account>();
		this.frasco = new ConcurrentHashMap<UUID, Transaction>();
	}
	
	
	public String toString() {
		
		return mappa.toString();
		
	}
	
	public String newAccount(String name,String surname)
	{
		Account c = new Account(name, surname);
		
		UUID id = UUID.randomUUID();
		
		this.mappa.put(id, c);
		
		return id.toString();
		
		
	}
	
	public void removeAccount(String id) {
		
		this.mappa.remove(UUID.fromString(id));
		
	}
	
	
	
	public Account getAccount(String id) {
		
		return this.mappa.get(UUID.fromString(id));
		
	}
	
	public void updateName(String id,String name) {
		
		Account c = this.getAccount(id);
		
		c.setName(name);
		
	}
	
	
	public void updateSurname(String id,String surname) {
		
		Account c = this.getAccount(id);
		
		c.setSurname(surname);
		
	}
	
	public String transfer(String a1, String a2,double amount) throws BalanceException{
		
		
		
		mappa.get(UUID.fromString(a1)).transfer(amount * -1);
		mappa.get(UUID.fromString(a2)).transfer(amount);
		
		Transaction t = new Transaction(UUID.fromString(a1),UUID.fromString(a2),amount);
		
		UUID id =  UUID.randomUUID();
		
		this.frasco.put(id, t);
		
		return id.toString();
		
	
		}
	
	public String divert(String transactionId) throws BalanceException {
		
		Transaction t = this.frasco.get(UUID.fromString(transactionId));
		
		UUID a1 = t.getRecipient();
		UUID a2 = t.getSender();
		double amount = t.getAmount();
		
	
		mappa.get(a1).transfer(amount * -1);
		mappa.get(a2).transfer(amount);
		
		 t = new Transaction(a1,a2,amount);
		 
		 UUID id = UUID.randomUUID();
		 
		 this.frasco.put(id, t);
		 
		 return id.toString();
		
		
		
		
	}
		
		
		
	}
	
