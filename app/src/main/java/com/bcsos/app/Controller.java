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

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//TODO
//controllare getTransferPage
//controllare Transaction

@SpringBootApplication
@RestController
public class Controller {
	public Map<String, String> parseBody(String bodyContent) {
		Map<String, String> body = new HashMap<String, String>();
		String[] values = bodyContent.split("&"); // split per '&' sul contenuto del body per ottenere una lista di
													// coppie chiave=valore
		for (int i = 0; i < values.length; ++i) {
			String[] coppia = values[i].split("="); // split per '=' per separare la chiave dal valore corrispondente
			if (coppia.length != 2) {
				continue;
			} else {
				body.put(coppia[0], coppia[1]);
			}
		}
		return body;
	}

	private String errorJSON(String error) {
		JSONObject o = new JSONObject();
		o.put("error", error);
		return o.toString();
	}

	public String errorJSON(Exception e) {
		JSONObject o = new JSONObject();
		o.put("error", e.getMessage());
		return o.toString();
	}

	@GetMapping("/api/account")
	public ResponseEntity<String> getAllAccounts() {
		JSONArray message = new JSONArray(AppApplication.bank.getAllAccounts().values().toArray());
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		return new ResponseEntity<String>(message.toString(), headers, HttpStatus.OK);
	}

	@GetMapping("/transfer")
	public String getTransferPage() throws URISyntaxException, IOException {

		URL res = getClass().getClassLoader().getResource("static/transfer.html");
		File file = Paths.get(res.toURI()).toFile();
		String absolutePath = file.getAbsolutePath();
		BufferedReader reader = new BufferedReader(new FileReader(absolutePath));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");

		try {
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}

			return stringBuilder.toString();
		} finally {
			reader.close();
		}
	}

	@GetMapping("/api/transaction") // aggiunta non richiesta
	public ResponseEntity<String> getAllTransaction() {
		JSONArray message = new JSONArray(AppApplication.bank.getAllTransaction().values().toArray());
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		return new ResponseEntity<String>(message.toString(), headers, HttpStatus.OK);
	}

	@GetMapping("/api/transfer/{accountId}") // aggiunta non richiesta
	public ResponseEntity<String> transaction(@PathVariable String accountId) {

		try {
			if (AppApplication.bank.getAccount(accountId) == null)
				return new ResponseEntity<String>(errorJSON("account not found"), HttpStatus.NOT_FOUND);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<String>(errorJSON("account not found") ,HttpStatus.NOT_FOUND);
		}
		
		Collection<com.bcsos.app.Transaction> map = AppApplication.bank.getAllTransaction().values();

		ArrayList<Transaction> arr = new ArrayList<Transaction>();

		for (Iterator iterator = map.iterator(); iterator.hasNext();) {
			Transaction transaction = (Transaction) iterator.next();

			boolean b = transaction.getSender().compareTo(UUID.fromString(accountId)) == 0
					|| transaction.getRecipient().compareTo(UUID.fromString(accountId)) == 0;

			if (b)
				arr.add(transaction);
		}

		Collections.sort(arr);
		Collections.reverse(arr);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		JSONArray message = new JSONArray(arr.toArray());
		return new ResponseEntity<String>(message.toString(), headers, HttpStatus.OK);

	}

	@RequestMapping(value = "/api/account", method = RequestMethod.POST)
	public ResponseEntity<String> addAccount(@RequestBody String bodyContent) {
		Map<String, String> body = parseBody(bodyContent);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		if (body == null)
			return new ResponseEntity<String>(errorJSON("something is wrong with the body of the request"), headers,
					HttpStatus.BAD_REQUEST);
		try {
			String id = AppApplication.bank.newAccount(body.get("name"), body.get("surname"));
			JSONObject message = new JSONObject();
			message.put("id", id);
			return new ResponseEntity<String>(message.toString(), headers, HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<String>(errorJSON(e), headers, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/api/{accountId}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteAccount(@PathVariable String accountId) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		try {
			if (AppApplication.bank.getAccount(accountId) == null)
				return new ResponseEntity<String>(HttpStatus.NOT_FOUND);

			AppApplication.bank.removeAccount(accountId);
			return new ResponseEntity<String>(HttpStatus.OK);

		} catch (IllegalArgumentException e) {

			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);

		}

	}

	@GetMapping("/api/account/{accountId}")
	public ResponseEntity<String> getAccount(@PathVariable String accountId) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		try {

			if (AppApplication.bank.getAccount(accountId) == null)
				return new ResponseEntity<String>(HttpStatus.NOT_FOUND);

			headers.add("X-Sistema-Bancario", String.join(";", AppApplication.bank.getAccount(accountId).getName(),
					AppApplication.bank.getAccount(accountId).getSurname()));
			JSONObject message = new JSONObject();
			message.put("balance", AppApplication.bank.getAccount(accountId).getBalance());
			message.put("surname", AppApplication.bank.getAccount(accountId).getSurname());
			message.put("name", AppApplication.bank.getAccount(accountId).getName());
			message.put("id", AppApplication.bank.getAccount(accountId).getId());
			return new ResponseEntity<String>(message.toString(), headers, HttpStatus.OK);

		} catch (IllegalArgumentException e) {

			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);

		}

	}

	@RequestMapping(value = "/api/account/{accountId}", method = RequestMethod.POST) // prelievo o deposito
	public ResponseEntity<String> operazione(@PathVariable String accountId, @RequestBody String bodyContent) {
		Map<String, String> body = parseBody(bodyContent);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		String transferId = "";
		if (body == null)
			return new ResponseEntity<String>(errorJSON("something is wrong with the body of the request"), headers,
					HttpStatus.BAD_REQUEST);
		if (body.get("amount") == null)
			return new ResponseEntity<String>(errorJSON("amount is null"), headers, HttpStatus.BAD_REQUEST);

		try {
			double amount = Double.parseDouble(body.get("amount"));
		} catch (NumberFormatException e) {
			return new ResponseEntity<String>(errorJSON("amount does not contain a parsable number"), headers,
					HttpStatus.BAD_REQUEST);
		}

		double amount = Double.parseDouble(body.get("amount"));
		if (amount == 0)
			return new ResponseEntity<String>(errorJSON("amount should not be 0"), headers, HttpStatus.OK);

		try {
			transferId = AppApplication.bank.transfer(accountId, accountId, amount);
		} catch (BalanceException | AccountNotFoundException e) {
			return new ResponseEntity<String>(errorJSON(e), headers, HttpStatus.OK);
		} catch (Exception e) {
			System.err.println("This should not happen...");
			e.getMessage();
			e.printStackTrace();
		}
		JSONObject o = new JSONObject();
		o.put("id", transferId);
		o.put("balance", AppApplication.bank.getAccount(accountId).getBalance());

		return new ResponseEntity<String>(o.toString(), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/account/{accountId}", method = RequestMethod.PUT)
	public ResponseEntity<String> updateAccountInformations(@PathVariable String accountId,
			@RequestBody String bodyContent) {
		Map<String, String> body = parseBody(bodyContent);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		if (AppApplication.bank.getAccount(accountId) == null)
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		if (body == null)
			return new ResponseEntity<String>(errorJSON("something is wrong with the body of the request"), headers,
					HttpStatus.BAD_REQUEST);
		if (body.get("name") == null || body.get("surname") == null)
			return new ResponseEntity<String>(errorJSON("body arguments name and surname must be set"), headers,
					HttpStatus.BAD_REQUEST);

		AppApplication.bank.getAccount(accountId).setName(body.get("name"));
		AppApplication.bank.getAccount(accountId).setSurname(body.get("surname"));
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/api/account/{accountId}", method = RequestMethod.PATCH)
	public ResponseEntity<String> updateSingleInformation(@PathVariable String accountId,
			@RequestBody String bodyContent) {
		Map<String, String> body = parseBody(bodyContent);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		if (AppApplication.bank.getAccount(accountId) == null)
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		if (body == null)
			return new ResponseEntity<String>(errorJSON("something is wrong with the body of the request"), headers,
					HttpStatus.BAD_REQUEST);

		if (body.get("name") != null && body.get("surname") == null) {
			AppApplication.bank.getAccount(accountId).setName(body.get("name"));
		} else if (body.get("name") == null && body.get("surname") != null) {
			AppApplication.bank.getAccount(accountId).setSurname(body.get("surname"));
		} else
			return new ResponseEntity<String>(errorJSON("body arguments name or (exclusive) surname must be set"),
					headers, HttpStatus.BAD_REQUEST);
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/api/account/{accountId}", method = RequestMethod.HEAD)
	public ResponseEntity<String> getInformazioni(@PathVariable String accountId) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		if (AppApplication.bank.getAccount(accountId) == null)
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);

		headers.add("X-Sistema-Bancario", String.join(";", AppApplication.bank.getAccount(accountId).getName(),
				AppApplication.bank.getAccount(accountId).getSurname()));
		return new ResponseEntity<String>(headers, HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/api/transfer", method = RequestMethod.POST)
	public ResponseEntity<String> transfer(@RequestBody String bodyContent) {
		String s = "";
		Map<String, String> body = parseBody(bodyContent);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		if (body == null)
			return new ResponseEntity<String>(errorJSON("something is wrong with the body of the request"), headers,
					HttpStatus.BAD_REQUEST);
		if (body.get("from") == null || body.get("to") == null || body.get("amount") == null)
			return new ResponseEntity<String>(errorJSON("body arguments from, to and amount must be set"), headers,
					HttpStatus.BAD_REQUEST);
		if (body.get("from").equals(body.get("to")))
			return new ResponseEntity<String>(errorJSON("sender and recipient must be different"), headers,
					HttpStatus.BAD_REQUEST);

		try {
			double amount = Double.parseDouble(body.get("amount"));
			if (amount == 0)
				return new ResponseEntity<String>(errorJSON("amount should not be 0"), headers, HttpStatus.BAD_REQUEST);
			if (amount < 0)
				return new ResponseEntity<String>(errorJSON("amount should not be negative"), headers,
						HttpStatus.BAD_REQUEST);

			s = AppApplication.bank.transfer(body.get("from"), body.get("to"), amount);

		} catch (BalanceException e) {
			return new ResponseEntity<String>(errorJSON(e), headers, HttpStatus.OK);
		} catch (AccountNotFoundException e) {
			return new ResponseEntity<String>(errorJSON(e), headers, HttpStatus.OK);

		} catch (IllegalArgumentException e) {

			return new ResponseEntity<String>(errorJSON("ids are not valid"), headers, HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			System.err.println("[FATAL ERROR] this shouldn't happen!");
			e.getClass();
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		JSONObject o = new JSONObject();
		o.put("id", body.get("from"));
		o.put("balance", AppApplication.bank.getAccount(body.get("from")).getBalance());
		o.put("id", body.get("to"));
		o.put("balance", AppApplication.bank.getAccount(body.get("to")).getBalance());

		return new ResponseEntity<String>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/api/divert", method = RequestMethod.POST)
	public ResponseEntity<String> divert(@RequestBody String bodyContent) {
		Map<String, String> body = parseBody(bodyContent);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		if (body == null)
			return new ResponseEntity<String>(errorJSON("something is wrong with the body of the request"), headers,
					HttpStatus.BAD_REQUEST);
		if (body.get("id") == null)
			return new ResponseEntity<String>(errorJSON("body argument id must be set"), headers,
					HttpStatus.BAD_REQUEST);

		try {
			AppApplication.bank.divert(body.get("id"));
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<String>(errorJSON(e), headers, HttpStatus.OK);
		} catch (BalanceException | AccountNotFoundException e) {
			return new ResponseEntity<String>(errorJSON(e), headers, HttpStatus.OK);
		} catch (Exception e) {
			System.err.println("[FATAL ERROR] this shouldn't happen!");
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}