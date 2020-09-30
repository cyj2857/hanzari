package com.hancom.hanzari;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = { "service", "com.hancom.hanzari.controllers", "configuration" })
@EnableJpaRepositories(basePackages = { "repository" })
@SpringBootApplication
public class HanzariProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(HanzariProjectApplication.class, args);
	}
}