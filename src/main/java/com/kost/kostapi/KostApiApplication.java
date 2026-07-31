package com.kost.kostapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.kost.kostapi.config.JwtProperties;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(JwtProperties.class)
public class KostApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(KostApiApplication.class, args);
	}

}
