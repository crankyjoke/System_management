package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.demo.Service.UserService;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {
	private final UserService userService;
	public DemoApplication(UserService userService) {
		this.userService = userService;
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	@Bean
	CommandLineRunner init() {
		return args -> {
			userService.createDefaultAdmin();
		};
	}
}
