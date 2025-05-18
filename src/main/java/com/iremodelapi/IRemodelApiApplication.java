package com.iremodelapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class IRemodelApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(IRemodelApiApplication.class, args);
    }
}