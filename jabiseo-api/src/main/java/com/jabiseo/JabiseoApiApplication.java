package com.jabiseo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class JabiseoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(JabiseoApiApplication.class, args);
    }
}
