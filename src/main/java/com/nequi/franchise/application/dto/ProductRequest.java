package com.nequi.franchise.application.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequest(

        @NotBlank(message = "{validation.product.name.notblank}")
        @Size(max = 100, message = "{validation.product.name.size}")
        String name,

        @Size(max = 500, message = "{validation.product.description.size}")
        String description,

        @NotNull(message = "{validation.product.price.notnull}")
        @DecimalMin(value = "0.0", inclusive = false, message = "{validation.product.price.min}")
        @Digits(integer = 10, fraction = 2, message = "{validation.product.price.digits}")
        BigDecimal price,

        @Size(max = 50, message = "{validation.product.category.size}")
        String category,

        boolean active

) {}
