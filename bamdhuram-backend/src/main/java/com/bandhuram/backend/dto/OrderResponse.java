package com.bandhuram.backend.dto;

import com.bandhuram.backend.entity.OrderStatus;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long id,
        String customerName,
        String phone,
        String notes,
        OrderStatus status,
        Instant createdAt,
        List<OrderItemResponse> items
) {}