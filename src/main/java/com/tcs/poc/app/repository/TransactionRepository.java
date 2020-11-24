package com.tcs.poc.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcs.poc.app.entity.TransactionRecord;

public interface TransactionRepository extends JpaRepository<TransactionRecord, Integer> {

	void save(double balance);
}