package com.nequi.franchise.application.service;

import com.nequi.franchise.application.dto.FranchiseRequest;
import com.nequi.franchise.application.dto.FranchiseResponse;
import com.nequi.franchise.domain.model.Franchise;
import com.nequi.franchise.infrastructure.mapper.FranchiseMapper;
import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseService {
    FranchiseMapper MAPPER = Mappers.getMapper(FranchiseMapper.class);
    
    Mono<Franchise> findById(String id);
    Flux<Franchise> findAll();
    Mono<Franchise> create(Franchise franchise);
    Mono<Franchise> update(String id, FranchiseRequest request);
    Mono<Void> delete(String id);
    
    default Franchise toEntity(FranchiseRequest request) {
        return MAPPER.toEntity(request);
    }
    
    default FranchiseResponse toResponse(Franchise franchise) {
        return MAPPER.toResponse(franchise);
    }
}
