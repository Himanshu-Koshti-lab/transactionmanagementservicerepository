package com.tcs.poc.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.poc.app.entity.TransactionRecord;
import com.tcs.poc.app.model.TransactionResponse;
import com.tcs.poc.app.service.TransactionService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	@PostMapping("/sendMoney")
	public TransactionResponse sendMoney(@RequestBody TransactionRecord transaction) {
		return transactionService.sendMoney(transaction);
	}

}