package com.streamhub.ott;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OttPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(OttPlatformApplication.class, args);
	}

}
