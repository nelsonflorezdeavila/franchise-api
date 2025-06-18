package com.nequi.franchise.presentation.controller.impl;

import com.nequi.franchise.application.dto.BranchRequest;
import com.nequi.franchise.application.dto.BranchResponse;
import com.nequi.franchise.application.service.BranchService;
import com.nequi.franchise.infrastructure.dto.ApiResponse;
import com.nequi.franchise.infrastructure.utils.CustomResponseBuilder;
import com.nequi.franchise.presentation.controller.BranchController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BranchControllerImpl implements BranchController {

    private final BranchService branchService;
    private final CustomResponseBuilder customResponseBuilder;

    @Override
    @GetMapping("/branches/{id}")
    public Mono<ResponseEntity<ApiResponse<BranchResponse>>> getBranch(@PathVariable String id) {
        return branchService.findById(id)
                .map(customResponseBuilder::success)
                .defaultIfEmpty(customResponseBuilder.notFound());
    }

    @Override
    @GetMapping("/franchises/{franchiseId}/branches")
    public Mono<ResponseEntity<ApiResponse<List<BranchResponse>>>> getBranchesByFranchise(@PathVariable String franchiseId) {
        return branchService.findByFranchiseId(franchiseId)
                .collectList()
                .map(customResponseBuilder::success);
    }

    @Override
    @GetMapping("/branches")
    public Mono<ResponseEntity<ApiResponse<List<BranchResponse>>>> getAllBranches() {
        return branchService.findAll()
                .collectList()
                .map(customResponseBuilder::success);
    }

    @Override
    @PostMapping("/franchises/{franchiseId}/branches")
    public Mono<ResponseEntity<ApiResponse<BranchResponse>>> createBranch(@PathVariable String franchiseId, BranchRequest request) {
        return branchService.create(franchiseId, request)
                .map(branch -> customResponseBuilder.created(branch, "branch.created"));
    }

    @Override
    @PutMapping("/branches/{id}")
    public Mono<ResponseEntity<ApiResponse<BranchResponse>>> updateBranch(@PathVariable String id, BranchRequest request) {
        return branchService.update(id, request)
                .map(branch -> customResponseBuilder.success(branch, "branch.updated"))
                .defaultIfEmpty(customResponseBuilder.notFound());
    }

    @Override
    @DeleteMapping("/branches/{id}")
    public Mono<ResponseEntity<ApiResponse<Void>>> deleteBranch(@PathVariable String id) {
        return branchService.delete(id)
                .then(Mono.just(customResponseBuilder.deleted("branch.deleted")));
    }
}
