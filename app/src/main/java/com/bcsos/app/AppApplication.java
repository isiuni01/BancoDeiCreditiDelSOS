package com.bcsos.app;

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
			
//			bank = new Bank();
//			
//			String s =bank.newAccount("berto", "Il profe");
//			
//			String u = bank.newAccount("isa", "Il bello");
//			
//			bank.transfer(u, u, 50);
//			
//			bank.transfer(u, s, 20);
//			
//			bank.saveState();
			
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
		
	}

}
