package com.archloner.homeiotserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HomeIoTServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeIoTServerApplication.class, args);
	}

}
