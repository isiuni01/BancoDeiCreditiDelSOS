package com.bcsos.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Controller {
	public static void main(String[] args) {
		SpringApplication.run(Controller.class, args);
	}
	
	public Map<String,String> parseBody(String bodyContent) {
		Map<String, String> body = new HashMap<String, String>();
		String[] values = bodyContent.split("&");	//split per '&' sul contenuto del body per ottenere una lista di coppie chiave=valore
		for (int i = 0; i < values.length; ++i) {
			String[] coppia = values[i].split("=");	//split per '=' per separare la chiave dal valore corrispondente
		    if (coppia.length != 2) {
		    	continue;
		    } else {
		    	body.put(coppia[0], coppia[1]);
		    }
		  }
		return body;
	}
	
	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}
	
	
	@GetMapping("/api/account")
	public ConcurrentHashMap<UUID, Account> getAllAccounts() {
		return AppApplication.bank.getAllAccounts();
	}
	
	@RequestMapping(value = "/api/account", method=RequestMethod.POST)
	public ResponseEntity<String> addAccount(@RequestBody String bodyContent) {
		Map<String, String> body = parseBody(bodyContent);
		if(body != null) {
			if(body.get("name") != null && body.get("surname") != null) {
			String uuid = AppApplication.bank.newAccount(body.get("name"), body.get("surname"));
				return new ResponseEntity<String>(uuid, HttpStatus.CREATED);
			}
		}
		return new ResponseEntity<String>("Failed", HttpStatus.OK);
		
	}
	
	@RequestMapping(value="/api/account", method=RequestMethod.DELETE)
	public void deleteAccount(@RequestParam String id) {
	}
	
	@GetMapping("/api/account/{accountId}")
	public void getAccount(@PathVariable String accountId) {
		
	}
	
	@RequestMapping(value="/api/account/{accountId}", method=RequestMethod.POST)
	public void operazione(@RequestBody String bodyContent) {
		
	}
	
	@RequestMapping(value="/api/account/{accountId}", method=RequestMethod.PUT)
	public void updateAccountInformations(@PathVariable String accountId, @RequestBody String bodyContent) {
		
	}
	
	@RequestMapping(value="/api/account/{accountId}", method=RequestMethod.PATCH)
	public void updateSingleInformation(@PathVariable String accountId, @RequestBody String bodyContent) {
		
	}
	
	@RequestMapping(value="/api/account/{accountId}", method=RequestMethod.HEAD)
	public void getInformazioni(@PathVariable String accountId) {
		
	}
	
	@RequestMapping(value="/api/transfer", method=RequestMethod.POST)
	public void transfer(@RequestBody String bodyContent) {
		
	}
	
	@RequestMapping(value="/api/divert", method=RequestMethod.POST)
	public void divert(@RequestBody String bodyContent) {
		
	}
}