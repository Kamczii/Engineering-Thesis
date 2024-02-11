package org.once_a_day.sso.config;

import feign.RequestInterceptor;
import org.once_a_day.sso.interceptor.FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public RequestInterceptor basicAuthRequestInterceptor() {
        return new FeignRequestInterceptor();
    }
}
