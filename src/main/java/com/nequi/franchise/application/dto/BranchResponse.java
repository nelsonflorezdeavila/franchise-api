package com.nequi.franchise.application.dto;

import java.time.LocalDateTime;

public record BranchResponse(
        String id,
        String name,
        String address,
        String city,
        String phone,
        String email,
        String franchiseId,
        String franchiseName,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
