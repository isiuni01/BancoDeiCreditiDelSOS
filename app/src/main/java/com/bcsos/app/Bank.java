package com.bcsos.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;

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

	public String newAccount(String name, String surname) {
		if (name == null || surname == null) {
			throw new NullPointerException("name o surname sono nulli");
		}

		Account c = new Account(name, surname);

		UUID id = UUID.randomUUID();
		c.setId(id);

		this.mappa.put(id, c);

		return id.toString();

	}

	public void removeAccount(String id) throws IllegalArgumentException, NullPointerException {

		UUID s = null;

		if (id == null) {
			throw new NullPointerException("name o surname sono nulli");
		}

		s = UUID.fromString(id);

		this.mappa.remove(s);

	}

	public Account getAccount(String id) throws IllegalArgumentException {

		return this.mappa.get(UUID.fromString(id));

	}

	public void updateName(String id, String name) {

		Account c = this.getAccount(id);

		c.setName(name);

	}

	public void updateSurname(String id, String surname) {

		Account c = this.getAccount(id);

		c.setSurname(surname);

	}

	public ConcurrentHashMap<UUID, Account> getAllAccounts() {

		return mappa;
	}

	public String transfer(String a1, String a2, double amount) throws Exception {	
		if(a1 != null && a2 != null) {
			if(mappa.get(UUID.fromString(a1)) != null && mappa.get(UUID.fromString(a2)) != null) {
				if(!a1.equals(a2)) {
					mappa.get(UUID.fromString(a1)).transfer(amount * -1);
					mappa.get(UUID.fromString(a2)).transfer(amount);
				} else {
					mappa.get(UUID.fromString(a2)).transfer(amount);
				}
				Transaction t = new Transaction(UUID.fromString(a1), UUID.fromString(a2), amount);

				UUID id = UUID.randomUUID();

				this.frasco.put(id, t);
				t.setId(id);	//questa operazione dovrebbe essere eseguita da Transaction, senza la possibilità di set

				return id.toString();
			}
			throw new AccountNotFoundException("[FATAL ERROR] Not both accountIds are valid");
		}
		throw new IllegalArgumentException("[FATAL ERROR] null values are not accepted");
	}

	public String divert(String transactionId) throws BalanceException, AccountNotFoundException {
		if(this.frasco.get(UUID.fromString(transactionId)) != null) {
			Transaction t = this.frasco.get(UUID.fromString(transactionId));	//transaction to divert
			if (!t.isCanceled()) {	//if transaction is not been diverted yet
				UUID recipient = t.getRecipient();
				UUID sender = t.getSender();
				double amount = t.getAmount();
				UUID id = UUID.randomUUID();
				
				t = new Transaction(recipient, sender, amount);	//create a new transaction, with the recipient sending back to the sender
				
				this.frasco.put(id, t);	//add transaction to the bank registry
				return id.toString();
			}
		}
		throw new AccountNotFoundException("[FATAL ERROR] Not both accountIds are valid");
	}

	public ConcurrentHashMap<UUID, Transaction>  getAllTransaction() {
		return frasco;
	}

}
