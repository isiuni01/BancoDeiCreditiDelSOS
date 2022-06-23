package com.bcsos.app;

import java.io.IOException;
import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppApplication {

	public static Bank bank;
	public static void main(String[] args) {
		
		SpringApplication.run(AppApplication.class, args);
		
		try {
			bank = (Bank) Bank.load();
		} catch(Exception e) {
			bank = new Bank();
			
			String s =bank.newAccount("Tommaso", "Rocca");
			
			String u = bank.newAccount("Isaia","Del Rosso");
			
			try {
				bank.transfer(u, u, 50);
				bank.transfer(u, s, 20);
				
				try {
					bank.saveState();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (AccountNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (BalanceException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
		}
		
		
		
		
		
		
	}

}
