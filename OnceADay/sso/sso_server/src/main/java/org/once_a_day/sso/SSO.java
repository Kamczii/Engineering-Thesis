package org.once_a_day.sso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableAutoConfiguration
@SpringBootApplication
@EnableFeignClients
@EntityScan("org.once_a_day.database.model")
public class SSO {
    public static void main(String[] args) {
        SpringApplication.run(SSO.class, args);
    }
}