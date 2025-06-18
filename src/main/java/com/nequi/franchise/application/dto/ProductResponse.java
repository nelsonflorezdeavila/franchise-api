package com.nequi.franchise.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponse(

        String id,
        String name,
        String description,
        BigDecimal price,
        String category,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
