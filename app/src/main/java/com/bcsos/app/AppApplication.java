package com.bcsos.app;

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
			e.printStackTrace();
		}
		
		
		
		
		
		
	}

}
