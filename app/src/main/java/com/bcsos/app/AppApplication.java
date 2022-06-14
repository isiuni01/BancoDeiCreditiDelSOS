package com.bcsos.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppApplication {

	public static Bank bank;
	public static void main(String[] args) {
		
		SpringApplication.run(AppApplication.class, args);
		
		bank = new Bank();
		
		bank.newAccount("Riccardo", "Berto");
		bank.newAccount("Isi", "Bello");
		
		
	}

}
