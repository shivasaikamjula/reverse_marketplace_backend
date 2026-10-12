package com.marketplace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Reverse Marketplace Spring Boot backend.
 *
 * Viva Note:
 * @SpringBootApplication is a convenience annotation that combines:
 * 1. @Configuration: Tags the class as the source of bean definitions.
 * 2. @EnableAutoConfiguration: Tells Spring Boot to configure beans based on classpath settings.
 * 3. @ComponentScan: Automatically scans and registers components, controllers, services,
 *    and repositories in package 'com.marketplace' and subpackages.
 */
@SpringBootApplication
public class ReverseMarketplaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReverseMarketplaceApplication.class, args);
        System.out.println("=================================================");
        System.out.println("Reverse Marketplace Backend is ready and running!");
        System.out.println("API Port: 8080");
        System.out.println("=================================================");
    }
}
