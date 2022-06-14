package com.bcsos.app;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;


public class Transaction {
	
	private UUID sender;
	private UUID recipient;
	private double amount;
	private LocalDateTime date;
	
	public Transaction(UUID sender, UUID recipient, double amount) {
		this.sender = sender;
		this.recipient = recipient;
		this.amount = amount;
		DateTimeFormatter temp = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		date = LocalDateTime.now();  
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
	
	
	
	
	
	
	
	
	
	
	



}
