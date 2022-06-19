package com.bcsos.app;

import java.io.BufferedReader;
import java.io.File;
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
import java.util.Map;
import java.util.UUID;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;

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
	public ResponseEntity<String> getAccount(@PathVariable String accountId) {
		
		if(AppApplication.bank.getAccount(accountId) != null) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("X-Sistema-Bancario",
		    		String.join(";",
		    				AppApplication.bank.getAccount(accountId).getName(),
		    				AppApplication.bank.getAccount(accountId).getSurname()));
			String message = AppApplication.bank.getAccount(accountId).toString();
			message += AppApplication.bank.getAllTransaction();
			return new ResponseEntity<String>(message, headers, HttpStatus.OK);
		}
		return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(value="/api/account/{accountId}", method=RequestMethod.POST)
	public ResponseEntity<String> operazione(@PathVariable String accountId, @RequestBody String bodyContent) {
		Map<String, String> body = parseBody(bodyContent);
		String transferId = "";
		if(AppApplication.bank.getAccount(accountId) != null) {
			if(body != null) {
				if(body.get("amount") != null) {
					double amount = Double.parseDouble(body.get("amount"));
					if(amount == 0)
						return new ResponseEntity<String>("Failed - amount should not be 0", HttpStatus.OK);
					try {
						transferId = AppApplication.bank.transfer(accountId, accountId, amount);
					} catch (BalanceException e) {
						return new ResponseEntity<String>("Failed - you don't have the funds to do that!", HttpStatus.OK);
					} catch (Exception e) {
						System.err.println("This should not happen...");
						e.getMessage();
						e.printStackTrace();
					}
					//TODO
					//potremmo inserire un'altro blocco catch per AccountNotFoundException
					//al posto che aggiungere il primo if(AppApplication.bank.getAccount(accountId) != null)
					
					//l'if iniziale ci da una migliore performance
					//la gestionde dell'eccezione probabilmente una migliore leggibilità
					String message = "TransferId: " + transferId + "\n";
					message += "Balance: " + AppApplication.bank.getAccount(accountId).getBalance();
					return new ResponseEntity<String>(message, HttpStatus.OK);
				}
				return new ResponseEntity<String>("Failed - amount is null", HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<String>("Failed - error occurred while parsing the body; Check your request body", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("Failed - the accountId seems to not exist; It should be something like this 72e6a2e7-5cd2-4fe4-a57d-7231587247ea", HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(value="/api/account/{accountId}", method=RequestMethod.PUT)
	public ResponseEntity<String> updateAccountInformations(@PathVariable String accountId, @RequestBody String bodyContent) {
		Map<String, String> body = parseBody(bodyContent);
		if(AppApplication.bank.getAccount(accountId) != null) {
			if(body != null) {
				if (body.get("name") != null && body.get("surname") != null) {
					AppApplication.bank.getAccount(accountId).setName(body.get("name"));
					AppApplication.bank.getAccount(accountId).setSurname(body.get("surname"));
					return new ResponseEntity<String>(HttpStatus.OK);
				}
				return new ResponseEntity<String>("Failed - body arguments name and surname must be set", HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<String>("Failed - error occurred while parsing the body; Check your request body", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(value="/api/account/{accountId}", method=RequestMethod.PATCH)
	public ResponseEntity<String> updateSingleInformation(@PathVariable String accountId, @RequestBody String bodyContent) {
		Map<String, String> body = parseBody(bodyContent);
		if(AppApplication.bank.getAccount(accountId) != null) {
			if(body != null) {
				if (body.get("name") != null && body.get("surname") == null) {
					AppApplication.bank.getAccount(accountId).setName(body.get("name"));
				} else if (body.get("name") == null && body.get("surname") != null){
					AppApplication.bank.getAccount(accountId).setSurname(body.get("surname"));
				} else
					return new ResponseEntity<String>("Failed - body arguments name or (exclusive) surname must be set", HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<String>("Failed - error occurred while parsing the body; Check your request body", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(value="/api/account/{accountId}", method=RequestMethod.HEAD)
	public ResponseEntity<String> getInformazioni(@PathVariable String accountId) {
		if(AppApplication.bank.getAccount(accountId) != null) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("X-Sistema-Bancario",
		    		String.join(";",
		    				AppApplication.bank.getAccount(accountId).getName(),
		    				AppApplication.bank.getAccount(accountId).getSurname()));
		    return new ResponseEntity<String>(headers, HttpStatus.OK);
		}
		return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
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