package com.nequi.franchise.infrastructure.utils;

import com.nequi.franchise.infrastructure.dto.ApiResponse;
import com.nequi.franchise.infrastructure.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomResponseBuilder {

    private final MessageService messageService;

    public <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    public <T> ResponseEntity<ApiResponse<T>> success(T data, String messageKey) {
        return ResponseEntity.ok(ApiResponse.success(data, messageService.getMessage(messageKey)));
    }

    public <T> ResponseEntity<ApiResponse<T>> created(T data, String messageKey) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(data, messageService.getMessage(messageKey)));
    }

    public <T> ResponseEntity<ApiResponse<T>> notFound() {
        return ResponseEntity.notFound().build();
    }

    public <T> ResponseEntity<ApiResponse<T>> deleted(String messageKey) {
        return ResponseEntity.ok(ApiResponse.success(null, messageService.getMessage(messageKey)));
    }
}
