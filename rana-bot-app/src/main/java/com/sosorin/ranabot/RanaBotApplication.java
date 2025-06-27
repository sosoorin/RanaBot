package com.sosorin.ranabot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author rana-bot
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class RanaBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(RanaBotApplication.class, args);
    }

}
