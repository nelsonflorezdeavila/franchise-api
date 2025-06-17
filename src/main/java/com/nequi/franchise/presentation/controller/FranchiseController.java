package com.nequi.franchise.presentation.controller;

import com.nequi.franchise.application.dto.FranchiseRequest;
import com.nequi.franchise.application.dto.FranchiseResponse;
import com.nequi.franchise.application.service.FranchiseService;
import com.nequi.franchise.infrastructure.aop.LogExecution;
import com.nequi.franchise.infrastructure.dto.ApiResponse;
import com.nequi.franchise.infrastructure.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/franchises")
@RequiredArgsConstructor
public class FranchiseController {

    private final FranchiseService franchiseService;
    private final MessageService messageService;

    @GetMapping("/{id}")
    @LogExecution("Get franchise by ID")
    public Mono<ResponseEntity<ApiResponse<FranchiseResponse>>> getFranchise(@PathVariable String id) {
        return franchiseService.findById(id)
            .map(franchiseService::toResponse)
            .map(franchise -> ResponseEntity.ok(ApiResponse.success(franchise)));
    }

    @GetMapping
    @LogExecution("Get all franchises")
    public Mono<ResponseEntity<ApiResponse<List<FranchiseResponse>>>> getAllFranchises() {
        return franchiseService.findAll()
            .map(franchiseService::toResponse)
            .collectList()
            .map(franchises -> ResponseEntity.ok(ApiResponse.success(franchises)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @LogExecution(value = "Create new franchise", logParameters = true)
    public Mono<ResponseEntity<ApiResponse<FranchiseResponse>>> createFranchise(
            @Valid @RequestBody FranchiseRequest request) {
        return franchiseService.create(franchiseService.toEntity(request))
            .map(franchiseService::toResponse)
            .map(franchise -> ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(franchise, messageService.getMessage("franchise.created"))));
    }

    @PutMapping("/{id}")
    @LogExecution(value = "Update franchise", logParameters = true)
    public Mono<ResponseEntity<ApiResponse<FranchiseResponse>>> updateFranchise(
            @PathVariable String id, 
            @Valid @RequestBody FranchiseRequest request) {
        return franchiseService.update(id, request)
            .map(franchiseService::toResponse)
            .map(franchise -> ResponseEntity
                .ok(ApiResponse.success(franchise, messageService.getMessage("franchise.updated"))));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @LogExecution("Delete franchise")
    public Mono<ResponseEntity<ApiResponse<Void>>> deleteFranchise(@PathVariable String id) {
        return franchiseService.delete(id)
            .then(Mono.just(ResponseEntity
                .ok(ApiResponse.success(null, messageService.getMessage("franchise.deleted")))));
    }
}
