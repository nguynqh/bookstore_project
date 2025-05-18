package com.bookstore.orderservice.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class ValidationErrorResponse extends ErrorResponse {
    private Map<String, String> errors;

    public ValidationErrorResponse(String code, String message, Map<String, String> errors) {
        super(code, message);
        this.errors = errors;
    }
}
