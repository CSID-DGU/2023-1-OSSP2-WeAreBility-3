package com.dongguk.cse.naemansan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NaemansanApplication {
	public static void main(String[] args) {
		SpringApplication.run(NaemansanApplication.class, args);
	}
}
