package com.tcs.poc.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.tcs.poc.app.model.AccountResponse;
import com.tcs.poc.app.model.TransactionRecordResponse;
import com.tcs.poc.app.model.TransactionRequest;
import com.tcs.poc.app.model.TransactionResponse;
import com.tcs.poc.app.model.UserResponse;
import com.tcs.poc.app.service.TransactionService;
import com.tcs.poc.app.utils.BankConstants;

@RestController
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

//	@PostMapping("/sendMoney")
//	public TransactionResponse sendMoney(@RequestBody TransactionRecord transaction) {
//		return transactionService.sendMoney(transaction);
//	}
	@PreAuthorize("hasRole('ROLE_CUSTOMER')")
	@PostMapping("/sendMoney")
	public TransactionResponse sendMoney(@RequestBody TransactionRequest transaction,
			@AuthenticationPrincipal String emailID,@RequestHeader("Authorization") String token) throws Exception {
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders  headers4 = new HttpHeaders();
		headers4.set("Authorization", token);
		headers4.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity4 = new HttpEntity<String>(headers4);
		ResponseEntity<UserResponse> responseU = restTemplate.exchange(BankConstants.USER_API_URL+"/getUser", HttpMethod.GET ,entity4 , UserResponse.class);
		UserResponse user = responseU.getBody();
		HttpHeaders  headers = new HttpHeaders();
		headers.set("Authorization", token);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		System.out.println("Calingrest");
		ResponseEntity<AccountResponse> responseA = restTemplate.exchange(BankConstants.ACCOUNT_API_URL+"/getAccount/"+transaction.getSenderAccountNumber(), HttpMethod.GET ,entity , AccountResponse.class);
		AccountResponse senderAccount = responseA.getBody();
		if (senderAccount.getUserId() == user.getUser_id()) {
			return transactionService.FundTransfer(transaction,token);
		} else {
			TransactionResponse response = new TransactionResponse();
			response.setTransactionStatus(0);
			response.setMessage("This is not logged user bank account");
			return response;		
			}
	}
	
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE')")
	@GetMapping(value = "/getCustomerTransaction")
	@ResponseBody
	public List<TransactionRecordResponse> getCustomerTransaction() {
		List<TransactionRecordResponse> user = transactionService.getCustomerTransaction();
		return user;
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE','ROLE_CUSTOMER')")
	@GetMapping(value = "/getMyTransaction")
	@ResponseBody
	public List<TransactionRecordResponse> getMyTransaction(@RequestHeader("Authorization") String token) {
		List<TransactionRecordResponse> user = transactionService.getMyTransaction(token);
		return user;
	}
	
	}