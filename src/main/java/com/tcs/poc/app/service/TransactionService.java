package com.tcs.poc.app.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcs.poc.app.entity.Account;
import com.tcs.poc.app.entity.TransactionRecord;
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


	public TransactionResponse sendMoney(TransactionRecord transaction) {
		Long senderAccountNumber = transaction.getSenderAccountNumber();
		Long receiverAccountNumber = transaction.getReceiverAccountNumber();
		Double amount = transaction.getAmount();
		TransactionResponse response = new TransactionResponse();
		Account senderAccount = accountRepository.findByAccountNumber(senderAccountNumber);
		Account receiverAccount = accountRepository.findByAccountNumber(receiverAccountNumber);
		if (senderAccount != null && receiverAccount != null && senderAccount.getBalance() >= amount) {
			senderAccount.setBalance(senderAccount.getBalance() - (amount));
			accountRepository.save(senderAccount);
			receiverAccount.setBalance(receiverAccount.getBalance() + (amount));
			accountRepository.save(receiverAccount);
			response = saveTransaction(transaction);

		}
		return response;
	}

	public TransactionResponse saveTransaction(TransactionRecord saveTransfer) {
		TransactionResponse response = new TransactionResponse();
		;
		TransactionRecord savetransfer = new TransactionRecord();

		Account senderAccount = accountRepository.findByAccountNumber(saveTransfer.getSenderAccountNumber());
		System.out.println(saveTransfer.getTransactionType());

		if (senderAccount != null) {
			savetransfer.setTransactionType("Debit");
		} else {
			savetransfer.setTransactionType("pending");
		}

		savetransfer.setSenderAccountNumber(saveTransfer.getSenderAccountNumber());
		savetransfer.setReceiverAccountNumber(saveTransfer.getReceiverAccountNumber());
		savetransfer.setAmount(saveTransfer.getAmount());
		savetransfer.setCreatedBy("system");
		savetransfer.setBalance(senderAccount.getBalance());
		savetransfer.setCreatedDate(new Date());
		savetransfer.setLastModifiedDate(new Date());
		savetransfer.setModifiedBy("system");
		savetransfer.setTransactionStatus(saveTransfer.getTransactionStatus());
		savetransfer.setUserId(senderAccount.getUserId());
		savetransfer.setTransStartTime(saveTransfer.getTransStartTime());
		savetransfer.setTransEndTime(saveTransfer.getTransEndTime());
		TransactionRecord re = transactionRepository.save(savetransfer);
		response.setReceiverAccountNumber(saveTransfer.getReceiverAccountNumber());
		response.setSenderAccountNumber(saveTransfer.getSenderAccountNumber());
		response.setTransactionId(re.getTransactionId());
		response.setAmount(saveTransfer.getAmount());
		response.setTransactionSuccessful("Success");
		return response;

	}
}