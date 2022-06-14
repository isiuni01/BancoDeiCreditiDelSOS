package com.bcsos.app;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Bank implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private ConcurrentHashMap<UUID, Account> mappa;
	
	public Bank()
	{
		this.mappa = new ConcurrentHashMap<UUID, Account>();
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
		
		return this.mappa.get(UUID.fromString(id));//va bene cosi o meglio String formattata??
		
	}
	
	public void updateName(String id,String name) {
		
		Account c = this.getAccount(id);
		
		c.setName(name);
		
	}
	
	
	public void updateSurname(String id,String surname) {
		
		Account c = this.getAccount(id);
		
		c.setSurname(surname);
		
	}
	
	
	
	
	
	
	
	

}
