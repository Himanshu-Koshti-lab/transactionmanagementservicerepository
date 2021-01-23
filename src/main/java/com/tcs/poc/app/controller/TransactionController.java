package com.tcs.poc.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.poc.app.model.TransactionRecordResponse;
import com.tcs.poc.app.model.TransactionRequest;
import com.tcs.poc.app.model.TransactionResponse;
import com.tcs.poc.app.service.TransactionService;

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
//		if (transaction.getEmailID().equals(emailID)) {
			return transactionService.FundTransfer(transaction,token);
//		} else {
//			TransactionResponse response = new TransactionResponse();
//			response.setTransactionStatus(0);
//			response.setMessage("loged user Mismatch");
//			return response;		
//			}
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