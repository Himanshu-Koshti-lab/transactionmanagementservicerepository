package com.tcs.poc.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin("*")
public class TransactionManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionManagementServiceApplication.class, args);
	}

}
