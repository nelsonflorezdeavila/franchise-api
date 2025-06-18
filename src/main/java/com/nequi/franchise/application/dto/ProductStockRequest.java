package com.nequi.franchise.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProductStockRequest(

        @NotNull(message = "{validation.stock.quantity.notnull}")
        @Min(value = 0, message = "{validation.stock.quantity.min}")
        Integer stock,

        @Min(value = 0, message = "{validation.stock.minstock.min}")
        Integer minStock,

        @Min(value = 1, message = "{validation.stock.maxstock.min}")
        Integer maxStock
) {}
