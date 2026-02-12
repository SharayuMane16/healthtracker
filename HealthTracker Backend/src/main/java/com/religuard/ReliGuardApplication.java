package com.religuard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReliGuardApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReliGuardApplication.class, args);
	}

}
