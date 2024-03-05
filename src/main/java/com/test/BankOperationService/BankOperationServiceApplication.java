package com.test.BankOperationService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
public class BankOperationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankOperationServiceApplication.class, args);
	}

}
