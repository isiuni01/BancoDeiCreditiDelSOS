package com.bcsos.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
import org.springframework.web.servlet.ModelAndView;

@SpringBootApplication
@RestController
public class Controller {
	
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
	public Object[] getAllAccounts() {
		
		Object[] list =  AppApplication.bank.getAllAccounts().values().toArray();
		
		return list;
	}
	
	@GetMapping("/transfer")
	public String getTransferPage() throws URISyntaxException, IOException {
		
		URL res = getClass().getClassLoader().getResource("static/transfer.html");
		File file = Paths.get(res.toURI()).toFile();
		String absolutePath = file.getAbsolutePath();
		BufferedReader reader = new BufferedReader(new FileReader (absolutePath));
	    String         line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	    String         ls = System.getProperty("line.separator");

	    try {
	        while((line = reader.readLine()) != null) {
	            stringBuilder.append(line);
	            stringBuilder.append(ls);
	        }

	        return stringBuilder.toString();
	    } finally {
	        reader.close();
	    }
	}
	
	@GetMapping("/api/transaction") //aggiunta non richiesta
	public Object[] getAllTransaction() {
		
		Object[] list =  AppApplication.bank.getAllTransaction().values().toArray();
		
		return list;
	}
	
	@GetMapping("/api/transfer/{accountId}") //aggiunta non richiesta
	public Object[] Transaction(@PathVariable String accountId) {
		
		Collection<com.bcsos.app.Transaction> map = AppApplication.bank.getAllTransaction().values();
		
		ArrayList<Transaction> arr = new ArrayList<Transaction>();
		
		for (Iterator iterator = map.iterator(); iterator.hasNext();) {
			Transaction transaction = (Transaction) iterator.next();
			
			boolean b = transaction.getSender().compareTo(UUID.fromString(accountId)) == 0 || transaction.getRecipient().compareTo(UUID.fromString(accountId)) == 0;
			
			if(b)
			{
				arr.add(transaction);
			}
			
		}
		
		Collections.sort(arr);
		Collections.reverse(arr);
		return arr.toArray();
	
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
	
	@RequestMapping(value="/api/{accountId}", method=RequestMethod.DELETE)
	public ResponseEntity<String> deleteAccount(@PathVariable String accountId) {
		
		try {
			
			AppApplication.bank.removeAccount(accountId);
			
			return new ResponseEntity<String>("OK",HttpStatus.OK);
			
		} catch (IllegalArgumentException  e) {
			return new ResponseEntity<String>("ERROR",HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping("/api/account/{accountId}")
	public Account getAccount(@PathVariable String accountId) {
		
		return AppApplication.bank.getAccount(accountId);
		
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
		
		Map<String, String> body = parseBody(bodyContent);
		System.out.println(body.get("from"));
		System.out.println(body.get("to"));
		System.out.println(body.get("amount"));
		
	}
	
	@RequestMapping(value="/api/divert", method=RequestMethod.POST)
	public void divert(@RequestBody String bodyContent) {
		
	}
}