package com.herring.felly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableMongoRepositories(basePackages = "com.herring.felly.repository")
public class FellyApplication {

	public static void main(String[] args) {
		SpringApplication.run(FellyApplication.class, args);
	}

}
