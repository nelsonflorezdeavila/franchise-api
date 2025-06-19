package com.nequi.franchise.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TopStockProductByBranchResponse(
        String branchId,
        String branchName,
        String branchAddress,
        String branchCity,
        String productId,
        String productName,
        String productDescription,
        BigDecimal productPrice,
        String productCategory,
        Integer stock,
        Integer minStock,
        Integer maxStock,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
