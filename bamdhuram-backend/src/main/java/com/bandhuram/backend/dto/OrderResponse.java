package com.bandhuram.backend.dto;

import com.bandhuram.backend.entity.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String customerName,
        String phone,
        String notes,
        OrderStatus status,
        LocalDateTime createdAt,
        List<OrderItemResponse> items
) {}