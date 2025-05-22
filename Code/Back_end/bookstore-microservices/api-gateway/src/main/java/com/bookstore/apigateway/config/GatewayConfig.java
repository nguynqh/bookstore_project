package com.bookstore.apigateway.config;

import com.bookstore.apigateway.filter.AuthenticationFilter;
import com.bookstore.apigateway.filter.LoggingFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.Arrays;
import java.util.List;

@Configuration
public class GatewayConfig {

    @Value("${public.paths}")
    private String publicPaths;

    @Bean
    public List<String> publicPathsList() {
        return Arrays.asList(publicPaths.split(","));
    }

    @Bean
    @Order(-1)
    public GlobalFilter loggingGlobalFilter(LoggingFilter loggingFilter) {
        return loggingFilter;
    }

    @Bean
    @Order(0)
    public GlobalFilter authenticationGlobalFilter(AuthenticationFilter authenticationFilter) {
        return authenticationFilter;
    }
}