package com.produce.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProducerSmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProducerSmsApplication.class, args);
	}

}
