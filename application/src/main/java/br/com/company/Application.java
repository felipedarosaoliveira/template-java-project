package br.com.company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot Application Entry Point
 * Infrastructure Layer - Bootstraps the application
 */
@SpringBootApplication(scanBasePackages = "br.com.company.*")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

