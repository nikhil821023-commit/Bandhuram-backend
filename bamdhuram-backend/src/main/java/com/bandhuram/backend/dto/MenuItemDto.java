package com.bandhuram.backend.dto;

public record MenuItemDto(
        Long id,
        String name,
        String description,
        String priceLabel,
        Integer sortOrder,
        boolean available,
        boolean featured,
        String photoUrl
) {}