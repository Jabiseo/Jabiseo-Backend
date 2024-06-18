package com.jabiseo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JabiseoApiApplication {

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "application-infrastructure,application-domain");
        SpringApplication.run(JabiseoApiApplication.class, args);
    }
}
