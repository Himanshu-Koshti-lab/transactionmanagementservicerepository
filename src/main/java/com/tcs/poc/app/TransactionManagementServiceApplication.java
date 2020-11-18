package com.tcs.poc.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class TransactionManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionManagementServiceApplication.class, args);
	}

}
