package com.nequi.franchise.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record FranchiseRequest(

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be less than 100 characters")
    String name,
    
    @Size(max = 500, message = "Description must be less than 500 characters")
    String description,
    
    boolean active
) {}
