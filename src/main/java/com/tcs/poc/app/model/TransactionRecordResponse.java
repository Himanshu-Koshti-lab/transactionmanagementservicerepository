package com.tcs.poc.app.model;

import lombok.Data;

@Data
public class TransactionRecordResponse {

	private Integer transactionId;
	private long senderAccountNumber;
	private long receiverAccountNumber;
	private Double amount;
	private int transactionStatus;
	private String transactionType;
}
