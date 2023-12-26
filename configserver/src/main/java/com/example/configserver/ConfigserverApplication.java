package com.example.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

import java.util.HashMap;

@SpringBootApplication
@EnableConfigServer
public class ConfigserverApplication {
	public static void main(String[] args) {
		SpringApplication.run(ConfigserverApplication.class, args);
	}

}
