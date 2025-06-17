package com.nequi.franchise.application.dto;

import java.time.LocalDateTime;

public record FranchiseResponse(
    String id,
    String name,
    String description,
    boolean active,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
