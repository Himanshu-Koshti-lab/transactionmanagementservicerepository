package com.tcs.poc.app.model;

import lombok.Data;

@Data
public class TransactionRequest {
	private String emailID;
	private long senderAccountNumber;
	private long receiverAccountNumber;
	private Double amount;
	//private String transactionSuccessful;
}