package com.bandhuram.backend.dto;

public record OrderItemResponse(
        Long menuItemId,
        String name,
        String priceLabel,
        int quantity
) {}