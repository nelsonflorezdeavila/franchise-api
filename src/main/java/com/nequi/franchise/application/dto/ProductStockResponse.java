package com.nequi.franchise.application.dto;

import java.time.LocalDateTime;

public record ProductStockResponse(
        String id,
        String productId,
        String productName,
        String branchId,
        String branchName,
        Integer stock,
        Integer minStock,
        Integer maxStock,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
