package org.once_a_day.file_storage.file_storage_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

import java.net.URI;

@Configuration
public class AWSConfig {
    @Bean
    S3Client s3Client() {
        final AwsBasicCredentials credentials = AwsBasicCredentials.create("test", "test");
        return S3Client.builder()
                .endpointOverride(URI.create("http://localstack:4566"))
                .region(Region.EU_CENTRAL_1)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .forcePathStyle(true)
                .build();
    }
}
