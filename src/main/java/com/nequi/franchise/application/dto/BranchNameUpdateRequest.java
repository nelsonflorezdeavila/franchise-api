package com.nequi.franchise.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BranchNameUpdateRequest(

        @NotBlank(message = "{validation.branch.name.notblank}")
        @Size(max = 100, message = "{validation.branch.name.size}")
        String name
) {
}
