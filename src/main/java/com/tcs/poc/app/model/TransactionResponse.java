package com.tcs.poc.app.model;

import lombok.Data;

@Data
public class TransactionResponse {
	private int transactionStatus;
	private String message;
}