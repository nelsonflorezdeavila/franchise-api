package com.nequi.franchise.infrastructure.service;

import com.nequi.franchise.application.dto.FranchiseRequest;
import com.nequi.franchise.application.service.FranchiseService;
import com.nequi.franchise.domain.model.Franchise;
import com.nequi.franchise.domain.repository.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FranchiseServiceImpl implements FranchiseService {

    private final FranchiseRepository franchiseRepository;

    @Override
    public Mono<Franchise> findById(String id) {
        return franchiseRepository.findById(id);
    }

    @Override
    public Flux<Franchise> findAll() {
        return franchiseRepository.findAll();
    }

    @Override
    public Mono<Franchise> create(Franchise franchise) {
        return franchiseRepository.save(franchise);
    }

    @Override
    public Mono<Franchise> update(String id, FranchiseRequest request) {
        return franchiseRepository.findById(id)
            .flatMap(existing -> {
                Franchise franchise = toEntity(request);
                franchise.setId(id);
                return franchiseRepository.save(franchise);
            });
    }

    @Override
    public Mono<Void> delete(String id) {
        return franchiseRepository.deleteById(id);
    }
}
