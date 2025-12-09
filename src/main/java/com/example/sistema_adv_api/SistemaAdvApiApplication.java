package com.example.sistema_adv_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SistemaAdvApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(SistemaAdvApiApplication.class, args);
    }
}
