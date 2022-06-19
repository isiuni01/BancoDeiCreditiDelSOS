package com.bcsos.app;

import java.time.LocalDateTime;
import java.util.UUID;


public class Transaction implements Comparable<Transaction> {
	
	private UUID id;
	private UUID sender;
	private UUID recipient;
	private double amount;
	private LocalDateTime date;
	private boolean canceled;
	
	public Transaction(UUID sender, UUID recipient, double amount) {
		this.sender = sender;
		this.recipient = recipient;
		this.amount = amount;
//		DateTimeFormatter temp = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
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

	public void setId(UUID id) {
		this.id = id;
	}
		
	
	public int compareTo(Transaction compareT) {
        return this.getDate().compareTo(compareT.getDate());
    }
  
	
	
	
	
	
	
	



}
