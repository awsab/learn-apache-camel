package org.me.practise.personaggregator;

import io.micrometer.tracing.Tracer;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PersonAggregatorApplication {

    public static void main(String[] args) {
        SpringApplication.run ( PersonAggregatorApplication.class, args );
    }

}
