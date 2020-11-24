package com.tcs.poc.app.model;

import lombok.Data;

@Data
public class TransactionResponse {
	private int transactionId;
	private long senderAccountNumber;
	private long receiverAccountNumber;
	private Double amount;
	private String transactionSuccessful;
}