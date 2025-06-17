package com.nequi.franchise.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record FranchiseRequest(

    @NotBlank(message = "{validation.name.notblank}")
    @Size(max = 100, message = "{validation.name.size}")
    String name,
    
    @Size(max = 500, message = "{validation.description.size}")
    String description,
    
    boolean active
) {}
