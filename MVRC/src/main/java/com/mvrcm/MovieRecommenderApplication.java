package com.mvrcm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@SpringBootApplication
public class MovieRecommenderApplication  {
    public static void main(String[] args) {
        SpringApplication.run(MovieRecommenderApplication.class, args);
    }
}
