package com.bookstore.apigateway.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
@Order(-2) // High precedence to catch exceptions before default handlers
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        DataBufferFactory bufferFactory = response.bufferFactory();

        if (ex instanceof ResponseStatusException responseStatusException) {
            response.setStatusCode(responseStatusException.getStatusCode());
        } else {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("status", response.getStatusCode().value());
        errorDetails.put("error", response.getStatusCode().toString());
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("path", exchange.getRequest().getPath().value());

        try {
            log.error("Error occurred: {}", ex.getMessage(), ex);
            return response.writeWith(
                    Mono.just(bufferFactory.wrap(objectMapper.writeValueAsBytes(errorDetails)))
            );
        } catch (JsonProcessingException e) {
            log.error("Error writing error response", e);
            return Mono.error(e);
        }
    }
}