package com.bandhuram.backend.dto;

import java.time.LocalDateTime;

public record ShopImageResponse(
        Long id,
        String url,
        String caption,
        Integer sortOrder,
        LocalDateTime uploadedAt
) {}