package com.tcs.poc.app.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tcs.poc.app.entity.TransactionRecord;
import com.tcs.poc.app.model.AccountResponse;
import com.tcs.poc.app.model.TransactionRecordResponse;
import com.tcs.poc.app.model.TransactionRequest;
import com.tcs.poc.app.model.TransactionResponse;
import com.tcs.poc.app.model.UserResponse;
import com.tcs.poc.app.repository.TransactionRepository;
import com.tcs.poc.app.utils.BankConstants;


@Service
public class TransactionService {

	TransactionService transactionService;

//	@Autowired
//	public AccountRepository accountRepository;

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

	public TransactionResponse FundTransfer(TransactionRequest request, String token) {
		Long senderAccountNumber = request.getSenderAccountNumber();
		Long receiverAccountNumber = request.getReceiverAccountNumber();
		
		Double amount = request.getAmount();
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders  headers = new HttpHeaders();
		headers.set("Authorization", token);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		System.out.println("Calingrest");
		ResponseEntity<AccountResponse> responseA = restTemplate.exchange(BankConstants.ACCOUNT_API_URL+"/getAccount/"+senderAccountNumber, HttpMethod.GET ,entity , AccountResponse.class);
		AccountResponse senderAccount = responseA.getBody();
		ResponseEntity<AccountResponse> responseB = restTemplate.exchange(BankConstants.ACCOUNT_API_URL+"/getAccount/"+receiverAccountNumber, HttpMethod.GET ,entity , AccountResponse.class);
		AccountResponse receiverAccount = responseB.getBody();
		System.out.println("Calingrestend");
		
		
//		Account senderAccount = accountRepository.findByAccountNumber(senderAccountNumber);
//		Account receiverAccount = accountRepository.findByAccountNumber(receiverAccountNumber);
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
			//accountRepository.save(senderAccount);
			HttpHeaders headers2 = new HttpHeaders();
			headers2.set("Authorization", token);
		    headers2.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		    HttpEntity<AccountResponse> entity1 = new HttpEntity<AccountResponse>(senderAccount,headers2);
		    System.out.println("update start");
		    restTemplate.exchange(BankConstants.ACCOUNT_API_URL+"/upDateBalance", HttpMethod.POST, entity1, String.class);
		    System.out.println("upadte sender done");
			receiverAccount.setBalance(receiverAccount.getBalance() + (amount));
			//accountRepository.save(receiverAccount);
			 HttpEntity<AccountResponse> entity2 = new HttpEntity<AccountResponse>(receiverAccount,headers2);
			 System.out.println("update  recv start");
			 restTemplate.exchange(BankConstants.ACCOUNT_API_URL+"/upDateBalance", HttpMethod.POST, entity2, String.class);
			 System.out.println("upadte rece done");
			// Sender Transaction Record Start//
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

	

	public List<TransactionRecordResponse> getCustomerTransaction() {
		List<TransactionRecord> tempTransaction = transactionRepository.findAll();
		List<TransactionRecordResponse> tempCustomer = new ArrayList<TransactionRecordResponse>();

		for (int i = 0; i < tempTransaction.size(); i++) {

			TransactionRecordResponse tempCustomer1 = new TransactionRecordResponse();
			tempCustomer1.setTransactionId(tempTransaction.get(i).getTransactionId());
			tempCustomer1.setSenderAccountNumber(tempTransaction.get(i).getSenderAccountNumber());
			tempCustomer1.setReceiverAccountNumber(tempTransaction.get(i).getReceiverAccountNumber());
			tempCustomer1.setAmount(tempTransaction.get(i).getAmount());
			tempCustomer1.setTransactionStatus(tempTransaction.get(i).getTransactionStatus());
			tempCustomer1.setTransactionType(tempTransaction.get(i).getTransactionType());
			tempCustomer.add(tempCustomer1);

		}
		return tempCustomer;

	}



	public List<TransactionRecordResponse> getMyTransaction(String token) {
		System.out.println("Inside getMyTransaction");
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders  headers4 = new HttpHeaders();
		headers4.set("Authorization", token);
		headers4.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity4 = new HttpEntity<String>(headers4);
		ResponseEntity<UserResponse> responseU = restTemplate.exchange(BankConstants.USER_API_URL+"/getUser", HttpMethod.GET ,entity4 , UserResponse.class);
		UserResponse user = responseU.getBody();
		List<TransactionRecord> tempTransaction = transactionRepository.findAll();
		List<TransactionRecordResponse> tempCustomer = new ArrayList<TransactionRecordResponse>();

		for (int i = 0; i < tempTransaction.size(); i++) {
			if(user.getUser_id() == tempTransaction.get(i).getUserId()) {
				TransactionRecordResponse tempCustomer1 = new TransactionRecordResponse();
				tempCustomer1.setTransactionId(tempTransaction.get(i).getTransactionId());
				tempCustomer1.setSenderAccountNumber(tempTransaction.get(i).getSenderAccountNumber());
				tempCustomer1.setReceiverAccountNumber(tempTransaction.get(i).getReceiverAccountNumber());
				tempCustomer1.setAmount(tempTransaction.get(i).getAmount());
				tempCustomer1.setTransactionStatus(tempTransaction.get(i).getTransactionStatus());
				tempCustomer1.setTransactionType(tempTransaction.get(i).getTransactionType());
				tempCustomer.add(tempCustomer1);
			}	

		}
		return tempCustomer;
	}

}