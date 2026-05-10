package com.app.pfms;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PfmsApplication {
	public static void main(String[] args) {
		SpringApplication.run(PfmsApplication.class, args);
	}
}