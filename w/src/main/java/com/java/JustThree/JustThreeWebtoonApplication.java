package com.java.JustThree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JustThreeWebtoonApplication {
	public static void main(String[] args) {
		SpringApplication.run(JustThreeWebtoonApplication.class, args);
	}

}
