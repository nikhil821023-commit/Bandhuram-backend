package com.bandhuram.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MenuItemRequest(
        @NotNull(message = "categoryId is required") Long categoryId,
        @NotBlank(message = "Item name is required") String name,
        String description,
        @NotBlank(message = "priceLabel is required") String priceLabel,
        Integer sortOrder,
        Boolean available,
        Boolean featured
) {}