package com.bcsos.app;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;


public class Transaction implements Comparable<Transaction>, Serializable {
	
	private static final long serialVersionUID = 1L;
	private UUID id;
	private UUID sender;
	private UUID recipient;
	private double amount;
	private LocalDateTime date;
	private boolean canceled;
	
	public Transaction(UUID sender, UUID recipient, double amount) {
		this.id = UUID.randomUUID();
		this.sender = sender;
		this.recipient = recipient;
		this.amount = amount;
		date = LocalDateTime.now();  
		this.canceled = false;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public boolean cancel() {
		if(this.isCanceled()) {
			this.canceled = true;
			return true;
		}
		return false;
	}
		
	public UUID getSender() {
		return sender;
	}

	public UUID getRecipient() {
		return recipient;
	}

	public double getAmount() {
		return amount;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public UUID getId() {
		return id;
	}		
	
	public int compareTo(Transaction other) {
        return this.getDate().compareTo(other.getDate());
    }
}
