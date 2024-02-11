package org.once_a_day.file_storage.file_storage_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableAutoConfiguration
@SpringBootApplication
@EntityScan("org.once_a_day.database.model")
@EnableFeignClients
public class FileStorage {
    public static void main(String[] args) {
        SpringApplication.run(FileStorage.class);
    }
}