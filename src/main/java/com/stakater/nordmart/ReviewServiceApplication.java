package com.stakater.nordmart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stakater.nordmart.service.ReviewServiceImpl;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.HashMap;


@SpringBootApplication
public class ReviewServiceApplication
{
    public static void main(String[] args) {
        SpringApplication.run(ReviewServiceApplication.class, args);
    }
}
