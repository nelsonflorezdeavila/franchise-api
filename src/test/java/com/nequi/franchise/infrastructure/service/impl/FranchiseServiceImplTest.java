package com.nequi.franchise.infrastructure.service.impl;

import com.nequi.franchise.application.dto.FranchiseRequest;
import com.nequi.franchise.application.dto.FranchiseResponse;
import com.nequi.franchise.domain.model.Franchise;
import com.nequi.franchise.domain.repository.FranchiseRepository;
import com.nequi.franchise.infrastructure.mapper.FranchiseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FranchiseServiceImplTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @Mock
    private FranchiseMapper franchiseMapper;

    @InjectMocks
    private FranchiseServiceImpl franchiseService;

    private Franchise franchise;
    private FranchiseResponse franchiseResponse;
    private FranchiseRequest franchiseRequest;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        franchise = Franchise.builder()
                .id("1")
                .name("Test Franchise")
                .description("Test Description")
                .active(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        franchiseResponse = new FranchiseResponse(
                franchise.getId(),
                franchise.getName(),
                franchise.getDescription(),
                franchise.isActive(),
                franchise.getCreatedAt(),
                franchise.getUpdatedAt()
        );

        franchiseRequest = new FranchiseRequest(
                "Test Franchise",
                "Test Description",
                true
        );
    }

    @Test
    void findById_WhenFranchiseExists_ShouldReturnFranchise() {
        when(franchiseRepository.findById(anyString())).thenReturn(Mono.just(franchise));
        when(franchiseMapper.toResponse(any(Franchise.class))).thenReturn(franchiseResponse);

        StepVerifier.create(franchiseService.findById("1"))
                .expectNext(franchiseResponse)
                .verifyComplete();
    }

    @Test
    void findById_WhenFranchiseDoesNotExist_ShouldReturnEmpty() {
        when(franchiseRepository.findById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(franchiseService.findById("1"))
                .verifyComplete();
    }

    @Test
    void findAll_ShouldReturnAllFranchises() {
        when(franchiseRepository.findAll()).thenReturn(Flux.just(franchise));
        when(franchiseMapper.toResponse(any(Franchise.class))).thenReturn(franchiseResponse);

        StepVerifier.create(franchiseService.findAll())
                .expectNext(franchiseResponse)
                .verifyComplete();
    }

    @Test
    void create_ShouldCreateFranchise() {
        when(franchiseMapper.toEntity(any(FranchiseRequest.class))).thenReturn(franchise);
        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));
        when(franchiseMapper.toResponse(any(Franchise.class))).thenReturn(franchiseResponse);

        StepVerifier.create(franchiseService.create(franchiseRequest))
                .expectNext(franchiseResponse)
                .verifyComplete();
    }

    @Test
    void update_WhenFranchiseExists_ShouldUpdateFranchise() {
        when(franchiseRepository.findById(anyString())).thenReturn(Mono.just(franchise));
        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));
        when(franchiseMapper.toResponse(any(Franchise.class))).thenReturn(franchiseResponse);

        StepVerifier.create(franchiseService.update("1", franchiseRequest))
                .expectNext(franchiseResponse)
                .verifyComplete();
    }

    @Test
    void update_WhenFranchiseDoesNotExist_ShouldReturnEmpty() {
        when(franchiseRepository.findById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(franchiseService.update("1", franchiseRequest))
                .verifyComplete();
    }

    @Test
    void delete_ShouldDeleteFranchise() {
        when(franchiseRepository.deleteById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(franchiseService.delete("1"))
                .verifyComplete();
    }

    @Test
    void updateName_WhenFranchiseExists_ShouldUpdateName() {
        when(franchiseRepository.findById(anyString())).thenReturn(Mono.just(franchise));
        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));
        when(franchiseMapper.toResponse(any(Franchise.class))).thenReturn(franchiseResponse);

        StepVerifier.create(franchiseService.updateName("1", "New Name"))
                .expectNext(franchiseResponse)
                .verifyComplete();
    }

    @Test
    void updateName_WhenFranchiseDoesNotExist_ShouldReturnEmpty() {
        when(franchiseRepository.findById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(franchiseService.updateName("1", "New Name"))
                .verifyComplete();
    }
}
