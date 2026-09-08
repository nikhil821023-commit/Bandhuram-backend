package com.bandhuram.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record OrderRequest(
        @NotBlank(message = "Name is required") @Size(max = 60) String customerName,
        @NotBlank(message = "Phone is required") @Size(max = 20) String phone,
        @Size(max = 300) String notes,
        @NotEmpty(message = "Your cart is empty") @Valid List<OrderItemRequest> items
) {}