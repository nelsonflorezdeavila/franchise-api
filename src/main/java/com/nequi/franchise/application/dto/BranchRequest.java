package com.nequi.franchise.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record BranchRequest(

        @NotBlank(message = "{validation.branch.name.notblank}")
        @Size(max = 100, message = "{validation.branch.name.size}")
        String name,

        @NotBlank(message = "{validation.branch.address.notblank}")
        @Size(max = 200, message = "{validation.branch.address.size}")
        String address,

        @NotBlank(message = "{validation.branch.city.notblank}")
        @Size(max = 50, message = "{validation.branch.city.size}")
        String city,

        @Pattern(regexp = "^[+]?[0-9\\s\\-()]{7,15}$", message = "{validation.branch.phone.pattern}")
        String phone,

        @Email(message = "{validation.branch.email.format}")
        @Size(max = 100, message = "{validation.branch.email.size}")
        String email,

        boolean active
) {}
