package com.bcsos.app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.springframework.http.HttpStatus;

//TODO
//add UUID check

public class Bank implements Serializable {

	private static final long serialVersionUID = 1L;

	private ConcurrentHashMap<UUID, Account> mappa;
	private ConcurrentHashMap<UUID, Transaction> frasco;

	public Bank() {
		this.mappa = new ConcurrentHashMap<UUID, Account>();
		this.frasco = new ConcurrentHashMap<UUID, Transaction>();
	}

	public String toString() {
		return mappa.toString();
	}

	public String newAccount(String name, String surname) throws IllegalArgumentException{
		if (name == null || surname == null || name.length() == 0 || surname.length() == 0)
			throw new IllegalArgumentException("[FATAL ERROR] name or surname are null");

		Account a = new Account(name, surname);
		this.mappa.put(a.getId(), a);
		
		
		try {
			this.saveState();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return a.getId().toString();
		
	}

	public void removeAccount(String id) throws IllegalArgumentException {
		if (id == null)
			throw new IllegalArgumentException("[FATAL ERROR] id is null");
		
		try {
			this.saveState();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.mappa.remove(UUID.fromString(id));
	}

	public Account getAccount(String id) throws IllegalArgumentException {
		if (id == null)
			throw new IllegalArgumentException("[FATAL ERROR] id is null");

		return this.mappa.get(UUID.fromString(id));
	}

	public void updateName(String id, String name) throws IllegalArgumentException, AccountNotFoundException {
		if(id == null || name == null || name.length() == 0)
			throw new IllegalArgumentException("[FATAL ERROR] name or name are null");
		
		Account c = this.getAccount(id);
		c.setName(name);
		
		try {
			this.saveState();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateSurname(String id, String surname) throws IllegalArgumentException, AccountNotFoundException {
		if(id == null || surname == null || surname.length() == 0)
			throw new IllegalArgumentException("[FATAL ERROR] name or surname are null");
			
		Account c = this.getAccount(id);
		c.setSurname(surname);
		
		try {
			this.saveState();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public ConcurrentHashMap<UUID, Account> getAllAccounts() {
		return mappa;
	}

	public String transfer(String a1, String a2, double amount) throws AccountNotFoundException, IllegalArgumentException, BalanceException {
		if(a1 == null || a2 == null)
			throw new IllegalArgumentException("[FATAL ERROR] sender, recipient or amount are null");
		if(mappa.get(UUID.fromString(a1)) == null && mappa.get(UUID.fromString(a2)) == null)
			throw new AccountNotFoundException("[FATAL ERROR] Not both accountIds are valid");
		
		if(!a1.equals(a2)) {
			mappa.get(UUID.fromString(a1)).transfer(amount * -1);
			mappa.get(UUID.fromString(a2)).transfer(amount);
		} else {
			mappa.get(UUID.fromString(a2)).transfer(amount);
		}
		Transaction t = new Transaction(UUID.fromString(a1), UUID.fromString(a2), amount);
		UUID id = t.getId();
		this.frasco.put(id, t);
		
		try {
			this.saveState();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return id.toString();
	}

	public String divert(String transactionId) throws BalanceException, AccountNotFoundException, IllegalArgumentException {
		if(transactionId == null)
			throw new IllegalArgumentException("[FATAL ERROR] transactionId is null");
		if(this.frasco.get(UUID.fromString(transactionId)) == null)
			throw new AccountNotFoundException("[FATAL ERROR] transactionId is not valid");
		
		Transaction t = this.frasco.get(UUID.fromString(transactionId));	//transaction to divert
		
		if(t.isCanceled())
			throw new IllegalArgumentException("this transaction is already been diverted");

		UUID recipient = t.getRecipient();
		UUID sender = t.getSender();
		double amount = t.getAmount();
		UUID id = UUID.randomUUID();
		
		t = new Transaction(recipient, sender, amount);	//create a new transaction, with the recipient sending back to the sender
		
		this.frasco.put(id, t);	//add transaction to the bank registry
		
		try {
			this.saveState();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return id.toString();
	}

	public ConcurrentHashMap<UUID, Transaction>  getAllTransaction() {
		return frasco;
	}
	
	public void saveState() throws IOException {
		
		 FileOutputStream file = new FileOutputStream("src/main/resources/data/data.dat");
        ObjectOutputStream out = new ObjectOutputStream(file);
              
        out.writeObject(this);
          
        out.close();
        file.close();
   }
	
	public static Bank load() throws ClassNotFoundException, IOException
	{
		
		FileInputStream file = new FileInputStream("src/main/resources/data/data.dat");
       ObjectInputStream in = new ObjectInputStream(file);
         
       Bank b = (Bank)in.readObject();
         
       in.close();
       file.close();
       
       return b;
	}

}
