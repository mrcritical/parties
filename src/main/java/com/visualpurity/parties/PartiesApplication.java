package com.visualpurity.parties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableCaching
@SpringBootApplication
@EnableReactiveMongoRepositories
public class PartiesApplication {

	public static void main(String[] args) {
		SpringApplication.run(PartiesApplication.class, args);
	}

}
