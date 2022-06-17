package com.bcsos.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppApplication {

	public static Bank bank;
	public static void main(String[] args) {
		
		SpringApplication.run(AppApplication.class, args);
		
		bank = new Bank();
		
		String c = bank.newAccount("Riccardo", "Berto");
		String c1 = bank.newAccount("Isi", "Bello");
		
		try {
			bank.getAccount(c).transfer(100);
			
			bank.transfer(c, c1, 50);
			
			bank.getAccount(c).transfer(100);
			
			bank.transfer(c, c1, 50);
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (BalanceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
