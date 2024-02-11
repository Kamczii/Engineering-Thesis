package org.once_a_day.messenger.messenger_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EntityScan({"org.once_a_day.database.model"})
@SpringBootApplication
@EnableFeignClients
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class);
    }
}