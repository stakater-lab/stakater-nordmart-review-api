package com.stakater.nordmart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.stakater.nordmart"})
public class ReviewServiceApplication {
    public static void main(final String[] args) {
        SpringApplication.run(ReviewServiceApplication.class, args);
    }
}
