package com.tcs.poc.app.service;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.tomcat.jni.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcs.poc.app.entity.Account;
import com.tcs.poc.app.entity.TransactionRecord;
import com.tcs.poc.app.model.TransactionRequest;
import com.tcs.poc.app.model.TransactionResponse;
import com.tcs.poc.app.repository.AccountRepository;
import com.tcs.poc.app.repository.TransactionRepository;

@Service
public class TransactionService {

	TransactionService transactionService;

	@Autowired
	public AccountRepository accountRepository;

	@Autowired
	public TransactionRepository transactionRepository;

//	public TransactionResponse sendMoney(TransactionRecord transaction) {
//		Long senderAccountNumber = transaction.getSenderAccountNumber();
//		Long receiverAccountNumber = transaction.getReceiverAccountNumber();
//		Double amount = transaction.getAmount();
//		TransactionResponse response = new TransactionResponse();
//		Account senderAccount = accountRepository.findByAccountNumber(senderAccountNumber);
//		Account receiverAccount = accountRepository.findByAccountNumber(receiverAccountNumber);
//		if (senderAccount != null && receiverAccount != null && senderAccount.getBalance() >= amount) {
//			senderAccount.setBalance(senderAccount.getBalance() - (amount));
//			accountRepository.save(senderAccount);
//			receiverAccount.setBalance(receiverAccount.getBalance() + (amount));
//			accountRepository.save(receiverAccount);
//			response = saveTransaction(transaction);
//
//		}
//		return response;
//	}
//
//	public TransactionResponse saveTransaction(TransactionRecord saveTransfer) {
//		TransactionResponse response = new TransactionResponse();
//		TransactionRecord savetransfer = new TransactionRecord();
//
//		Account senderAccount = accountRepository.findByAccountNumber(saveTransfer.getSenderAccountNumber());
//		System.out.println(saveTransfer.getTransactionType());
//
//		if (senderAccount != null) {
//			savetransfer.setTransactionType("Debit");
//		} else {
//			savetransfer.setTransactionType("pending");
//		}
//
//		savetransfer.setSenderAccountNumber(saveTransfer.getSenderAccountNumber());
//		savetransfer.setReceiverAccountNumber(saveTransfer.getReceiverAccountNumber());
//		savetransfer.setAmount(saveTransfer.getAmount());
//		savetransfer.setCreatedBy("system");
//		savetransfer.setBalance(senderAccount.getBalance());
//		savetransfer.setCreatedDate(new Date());
//		savetransfer.setLastModifiedDate(new Date());
//		savetransfer.setModifiedBy("system");
//		savetransfer.setTransactionStatus(saveTransfer.getTransactionStatus());
//		savetransfer.setUserId(senderAccount.getUserId());
//		savetransfer.setTransStartTime(saveTransfer.getTransStartTime());
//		savetransfer.setTransEndTime(saveTransfer.getTransEndTime());
//		TransactionRecord re = transactionRepository.save(savetransfer);
//		response.setReceiverAccountNumber(saveTransfer.getReceiverAccountNumber());
//		response.setSenderAccountNumber(saveTransfer.getSenderAccountNumber());
//		response.setTransactionId(re.getTransactionId());
//		response.setAmount(saveTransfer.getAmount());
//		response.setTransactionSuccessful("Success");
//		return response;
//
//	}

	public TransactionResponse FundTransfer(TransactionRequest request) throws Exception {
		Long senderAccountNumber = request.getSenderAccountNumber();
		Long receiverAccountNumber = request.getReceiverAccountNumber();
		Double amount = request.getAmount();
		System.out.println(senderAccountNumber +"  "+ receiverAccountNumber + "  "+ amount  );
		
		Account senderAccount = accountRepository.findByAccountNumber(senderAccountNumber);
		Account receiverAccount = accountRepository.findByAccountNumber(receiverAccountNumber);
		
		System.out.println(senderAccount.getAccountNumber() + " " + receiverAccount.getAccountNumber() + " " + amount );
		TransactionResponse response = new TransactionResponse();
		if (senderAccount == null) {
			System.out.println("Sender Khali hai");
			throw new Exception();
		} else if (receiverAccount == null) {
			System.out.println("Receiever ka bank hi nahi hai");
			throw new Exception();
		} else if (senderAccount.getBalance() <= amount) {
			System.out.println("ja gareeb mar ja");
			System.out.println("Fund transfer denied processing");
			TransactionRecord record = new TransactionRecord();
			record.setTransactionStatus(0);
			record.setCreatedBy("system");
			record.setCreatedDate(new Date());
			record.setModifiedBy("system");
			record.setLastModifiedDate(new Date());
			record.setUserId(senderAccount.getUserId());
			record.setBalance(senderAccount.getBalance());
			record.setAmount(amount);
			record.setSenderAccountNumber(senderAccountNumber);
			record.setReceiverAccountNumber(receiverAccountNumber);			
			TransactionRecord savedRecord = transactionRepository.save(record);
			response.setTransactionId(savedRecord.getTransactionId());
			response.setSenderAccountNumber(senderAccountNumber);
			response.setReceiverAccountNumber(receiverAccountNumber);
			response.setAmount(amount);
			response.setTransactionStatus(record.getTransactionStatus());
			return response;
		} else {
			System.out.println("Fund transfer processing");
			TransactionRecord record = new TransactionRecord();
			senderAccount.setBalance(senderAccount.getBalance() - (amount));
			accountRepository.save(senderAccount);
			receiverAccount.setBalance(receiverAccount.getBalance() + (amount));
			accountRepository.save(receiverAccount);
			record.setTransactionStatus(1);
			record.setCreatedBy("system");
			record.setCreatedDate(new Date());
			record.setModifiedBy("system");
			record.setLastModifiedDate(new Date());
			record.setUserId(senderAccount.getUserId());
			record.setBalance(senderAccount.getBalance());
			record.setAmount(amount);
			record.setSenderAccountNumber(senderAccountNumber);
			record.setReceiverAccountNumber(receiverAccountNumber);
			TransactionRecord savedRecord = transactionRepository.save(record);
			response.setTransactionId(savedRecord.getTransactionId());
			response.setSenderAccountNumber(senderAccountNumber);
			response.setReceiverAccountNumber(receiverAccountNumber);
			response.setAmount(amount);
			response.setTransactionStatus(1);
			return response;
		}
	}

	

}