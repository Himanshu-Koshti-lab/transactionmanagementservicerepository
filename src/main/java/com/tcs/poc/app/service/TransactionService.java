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

	public TransactionResponse FundTransfer(TransactionRequest request) {
		Long senderAccountNumber = request.getSenderAccountNumber();
		Long receiverAccountNumber = request.getReceiverAccountNumber();
		Double amount = request.getAmount();
		Account senderAccount = accountRepository.findByAccountNumber(senderAccountNumber);
		Account receiverAccount = accountRepository.findByAccountNumber(receiverAccountNumber);
		TransactionResponse response = new TransactionResponse();
		if (senderAccount == null) {
			response.setTransactionStatus(0);
			response.setMessage("Sender Account not Found");
			return response;
		} else if (receiverAccount == null) {
			response.setTransactionStatus(0);
			response.setMessage("Reciever Account not Found");
			return response;
		} else if (senderAccount.getBalance() < amount || amount <= 0) {
			TransactionRecord record = new TransactionRecord();
			record.setTransactionStatus(0);
			record.setCreatedBy("system");
			record.setCreatedDate(new Date());
			record.setModifiedBy("system");
			record.setTransactionType("Debit");
			record.setLastModifiedDate(new Date());
			record.setUserId(senderAccount.getUserId());
			record.setBalance(senderAccount.getBalance());
			record.setAmount(amount);
			record.setSenderAccountNumber(senderAccountNumber);
			record.setReceiverAccountNumber(receiverAccountNumber);
			TransactionRecord savedRecord = transactionRepository.save(record);
			response.setTransactionStatus(record.getTransactionStatus());
			response.setMessage("Transaction Failed");
			return response;
		} else {
			System.out.println("Fund transfer processing");
			TransactionRecord SenderRecord = new TransactionRecord();
			TransactionRecord RecieverRecord = new TransactionRecord();
			senderAccount.setBalance(senderAccount.getBalance() - (amount));
			accountRepository.save(senderAccount);
			receiverAccount.setBalance(receiverAccount.getBalance() + (amount));
			accountRepository.save(receiverAccount);
			//Sender Transaction Record Start//
			SenderRecord.setTransactionStatus(1);
			SenderRecord.setCreatedBy("system");
			SenderRecord.setCreatedDate(new Date());
			SenderRecord.setModifiedBy("system");
			SenderRecord.setLastModifiedDate(new Date());
			SenderRecord.setUserId(senderAccount.getUserId());
			SenderRecord.setBalance(senderAccount.getBalance());
			SenderRecord.setAmount(amount);
			SenderRecord.setTransactionType("Debit");
			SenderRecord.setSenderAccountNumber(senderAccountNumber);
			SenderRecord.setReceiverAccountNumber(receiverAccountNumber);
			transactionRepository.save(SenderRecord);
			RecieverRecord.setTransactionStatus(1);
			RecieverRecord.setCreatedBy("system");
			RecieverRecord.setCreatedDate(new Date());
			RecieverRecord.setModifiedBy("system");
			RecieverRecord.setLastModifiedDate(new Date());
			RecieverRecord.setUserId(receiverAccount.getUserId());
			RecieverRecord.setBalance(receiverAccount.getBalance());
			RecieverRecord.setAmount(amount);
			RecieverRecord.setTransactionType("Credit");
			RecieverRecord.setSenderAccountNumber(senderAccountNumber);
			RecieverRecord.setReceiverAccountNumber(receiverAccountNumber);
			transactionRepository.save(RecieverRecord);
			response.setTransactionStatus(SenderRecord.getTransactionStatus());
			response.setMessage("Transaction SuccessFull");
			return response;
		}
	}

}