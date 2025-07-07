package com.me.learning.consul.springinteg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpringIntegApplication {

    public static void main (String[] args) {
        SpringApplication.run (SpringIntegApplication.class, args);
    }

}
