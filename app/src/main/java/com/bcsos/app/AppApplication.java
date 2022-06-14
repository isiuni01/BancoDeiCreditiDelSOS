package com.bcsos.app;

import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppApplication {

	public static ArrayList<Account> a;
	public static void main(String[] args) {
		
		SpringApplication.run(AppApplication.class, args);
		
		a = new ArrayList<Account>();
		a.add(new Account("Riccardo", "Berto"));
		a.add(new Account("Isi","Bello"));
		
		
	}

}
