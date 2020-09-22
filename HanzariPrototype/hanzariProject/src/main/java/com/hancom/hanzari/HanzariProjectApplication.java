package com.hancom.hanzari;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.hancom.hanzariProject.controllers")
@SpringBootApplication
public class HanzariProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(HanzariProjectApplication.class, args);
	}
}