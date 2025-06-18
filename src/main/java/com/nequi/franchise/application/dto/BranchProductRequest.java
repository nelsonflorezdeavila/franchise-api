package com.nequi.franchise.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record BranchProductRequest(

        @Valid
        @NotNull(message = "{validation.product.notnull}")
        ProductRequest product,

        @NotNull(message = "{validation.stock.quantity.notnull}")
        @Min(value = 0, message = "{validation.stock.quantity.min}")
        Integer initialStock,

        @Min(value = 0, message = "{validation.stock.minstock.min}")
        Integer minStock,

        @Min(value = 1, message = "{validation.stock.maxstock.min}")
        Integer maxStock
) {}
