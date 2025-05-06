package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@SpringBootApplication
@EnableCircuitBreaker
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		System.out.println("http://localhost:8080");
		System.out.println("http://localhost:8080/actuator");
		System.out.println("http://localhost:8080/actuator/prometheus");
		System.out.println("http://localhost:8080/actuator/health");
		System.out.println("http://localhost:8080/actuator/health/diskSpace");
		System.out.println("http://localhost:8080/actuator/logfile");
		System.out.println("http://localhost:8080/browser/index.html");
		System.out.println("http://localhost:8080/datarest");
	}

}
