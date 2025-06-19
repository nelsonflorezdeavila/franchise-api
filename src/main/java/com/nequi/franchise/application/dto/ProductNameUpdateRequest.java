package com.nequi.franchise.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProductNameUpdateRequest(

        @NotBlank(message = "{validation.product.name.notblank}")
        @Size(max = 100, message = "{validation.product.name.size}")
        String name
) {
}
