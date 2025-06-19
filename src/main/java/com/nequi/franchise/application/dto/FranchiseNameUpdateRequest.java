package com.nequi.franchise.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FranchiseNameUpdateRequest(

        @NotBlank(message = "{validation.name.notblank}")
        @Size(max = 100, message = "{validation.name.size}")
        String name
) {
}
