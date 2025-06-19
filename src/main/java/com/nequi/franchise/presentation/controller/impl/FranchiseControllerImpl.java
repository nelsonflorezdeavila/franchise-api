package com.nequi.franchise.presentation.controller.impl;

import com.nequi.franchise.application.dto.FranchiseRequest;
import com.nequi.franchise.application.dto.FranchiseResponse;
import com.nequi.franchise.application.service.FranchiseService;
import com.nequi.franchise.infrastructure.dto.ApiResponse;
import com.nequi.franchise.infrastructure.utils.CustomResponseBuilder;
import com.nequi.franchise.presentation.controller.FranchiseController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/franchises")
@RequiredArgsConstructor
@Slf4j
public class FranchiseControllerImpl implements FranchiseController {

    private final FranchiseService franchiseService;
    private final CustomResponseBuilder customResponseBuilder;

    @Override
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ApiResponse<FranchiseResponse>>> getFranchise(@PathVariable String id) {
        return franchiseService.findById(id)
                .map(customResponseBuilder::success)
                .defaultIfEmpty(customResponseBuilder.notFound());
    }

    @Override
    @GetMapping
    public Mono<ResponseEntity<ApiResponse<List<FranchiseResponse>>>> getAllFranchises() {
        return franchiseService.findAll()
                .collectList()
                .map(customResponseBuilder::success);
    }

    @Override
    @PostMapping
    public Mono<ResponseEntity<ApiResponse<FranchiseResponse>>> createFranchise(
            FranchiseRequest request) {
        return franchiseService.create(request)
                .map(franchise -> customResponseBuilder.created(franchise, "franchise.created"));
    }

    @Override
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ApiResponse<FranchiseResponse>>> updateFranchise(
            @PathVariable String id, FranchiseRequest request) {
        return franchiseService.update(id, request)
                .map(franchise -> customResponseBuilder.success(franchise, "franchise.updated"))
                .defaultIfEmpty(customResponseBuilder.notFound());
    }

    @Override
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<ApiResponse<Void>>> deleteFranchise(@PathVariable String id) {
        return franchiseService.delete(id)
                .then(Mono.just(customResponseBuilder.deleted("franchise.deleted")));
    }
}
