package com.example.order.shared;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class RestResponseBuilder {

    public ResponseEntity<ErrorStructure> error(HttpStatus status, String errorMessage) {
        ErrorStructure error = ErrorStructure.builder()
                .status(status.value())
                .errorMessage(errorMessage)
                .build();
        return ResponseEntity.status(status).body(error);
    }
}
