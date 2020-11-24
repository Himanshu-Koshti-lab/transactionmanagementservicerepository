package com.tcs.poc.app.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TransactionRecord")
public class TransactionRecord {

	@Id
	@GeneratedValue
	@Column(name = "transactionId")
	private Integer transactionId;

	@Column(name = "userId")
	private Integer userId;

	@Column(name = "transStartTime")
	private Timestamp transStartTime;

	@Column(name = "transEndTime")
	private Timestamp transEndTime;

	@Column(name = "transactionType")
	private String transactionType;

	@Column(name = "transactionStatus")
	private int transactionStatus;

	@Column(name = "senderAccountNumber")
	private long senderAccountNumber;

	@Column(name = "receiverAccountNumber")
	private long receiverAccountNumber;

	@Column(name = "amount")
	private double amount;

	@Column(name = "balance")
	private double balance;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "lastModified_date")
	private Date lastModifiedDate;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "modified_by")
	private String modifiedBy;

}
