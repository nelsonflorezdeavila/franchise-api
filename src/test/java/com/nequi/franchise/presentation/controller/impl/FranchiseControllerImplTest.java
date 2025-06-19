package com.nequi.franchise.presentation.controller.impl;

import com.nequi.franchise.BaseIntegrationTest;
import com.nequi.franchise.application.dto.FranchiseNameUpdateRequest;
import com.nequi.franchise.application.dto.FranchiseRequest;
import com.nequi.franchise.domain.model.Franchise;
import com.nequi.franchise.domain.repository.FranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

class FranchiseControllerImplTest extends BaseIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private FranchiseRepository franchiseRepository;

    private Franchise testFranchise;
    private FranchiseRequest franchiseRequest;

    @BeforeEach
    void setUp() {
        // Clear the database before each test
        franchiseRepository.deleteAll().block();

        // Create test data
        testFranchise = Franchise.builder()
                .name("Test Franchise")
                .description("Test Description")
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        franchiseRequest = new FranchiseRequest(
                "Test Franchise",
                "Test Description",
                true
        );
    }

    @Test
    void getFranchise_WhenExists_ShouldReturnFranchise() {
        // Create a franchise in the database
        Franchise savedFranchise = franchiseRepository.save(testFranchise).block();

        webTestClient.get()
                .uri("/api/franchises/{id}", savedFranchise.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.data.name").isEqualTo(testFranchise.getName())
                .jsonPath("$.data.description").isEqualTo(testFranchise.getDescription());
    }

    @Test
    void getFranchise_WhenNotExists_ShouldReturnNotFound() {
        webTestClient.get()
                .uri("/api/franchises/non-existent-id")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getAllFranchises_ShouldReturnAllFranchises() {
        // Create multiple franchises
        franchiseRepository.save(testFranchise).block();
        
        Franchise anotherFranchise = Franchise.builder()
                .name("Another Franchise")
                .description("Another Description")
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        franchiseRepository.save(anotherFranchise).block();

        webTestClient.get()
                .uri("/api/franchises")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.data.length()").isEqualTo(2);
    }

    @Test
    void createFranchise_WithValidData_ShouldCreateFranchise() {
        webTestClient.post()
                .uri("/api/franchises")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(franchiseRequest), FranchiseRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.data.name").isEqualTo(franchiseRequest.name())
                .jsonPath("$.data.description").isEqualTo(franchiseRequest.description());
    }

    @Test
    void updateFranchise_WhenExists_ShouldUpdateFranchise() {
        // Create a franchise first
        Franchise savedFranchise = franchiseRepository.save(testFranchise).block();

        FranchiseRequest updateRequest = new FranchiseRequest(
                "Updated Name",
                "Updated Description",
                true
        );

        webTestClient.put()
                .uri("/api/franchises/{id}", savedFranchise.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), FranchiseRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.data.name").isEqualTo(updateRequest.name())
                .jsonPath("$.data.description").isEqualTo(updateRequest.description());
    }

    @Test
    void updateFranchise_WhenNotExists_ShouldReturnNotFound() {
        webTestClient.put()
                .uri("/api/franchises/non-existent-id")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(franchiseRequest), FranchiseRequest.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void deleteFranchise_WhenExists_ShouldDeleteFranchise() {
        // Create a franchise first
        Franchise savedFranchise = franchiseRepository.save(testFranchise).block();

        webTestClient.delete()
                .uri("/api/franchises/{id}", savedFranchise.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true);

        // Verify franchise is deleted
        webTestClient.get()
                .uri("/api/franchises/{id}", savedFranchise.getId())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void updateFranchiseName_WhenExists_ShouldUpdateName() {
        // Create a franchise first
        Franchise savedFranchise = franchiseRepository.save(testFranchise).block();

        FranchiseNameUpdateRequest nameUpdateRequest = new FranchiseNameUpdateRequest("New Name");

        webTestClient.patch()
                .uri("/api/franchises/{id}/name", savedFranchise.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(nameUpdateRequest), FranchiseNameUpdateRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.data.name").isEqualTo("New Name");
    }

    @Test
    void updateFranchiseName_WhenNotExists_ShouldReturnNotFound() {
        FranchiseNameUpdateRequest nameUpdateRequest = new FranchiseNameUpdateRequest("New Name");

        webTestClient.patch()
                .uri("/api/franchises/non-existent-id/name")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(nameUpdateRequest), FranchiseNameUpdateRequest.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void createFranchise_WithInvalidData_ShouldReturnBadRequest() {
        FranchiseRequest invalidRequest = new FranchiseRequest(
                "", // Empty name should be invalid
                "Test Description",
                true
        );

        webTestClient.post()
                .uri("/api/franchises")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(invalidRequest), FranchiseRequest.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void updateFranchiseName_WithInvalidName_ShouldReturnBadRequest() {
        Franchise savedFranchise = franchiseRepository.save(testFranchise).block();
        FranchiseNameUpdateRequest invalidRequest = new FranchiseNameUpdateRequest("");

        webTestClient.patch()
                .uri("/api/franchises/{id}/name", savedFranchise.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(invalidRequest), FranchiseNameUpdateRequest.class)
                .exchange()
                .expectStatus().isBadRequest();
    }
}
