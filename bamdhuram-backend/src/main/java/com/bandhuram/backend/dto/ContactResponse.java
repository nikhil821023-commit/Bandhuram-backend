package com.bandhuram.backend.dto;

import com.bandhuram.backend.entity.InquiryStatus;
import java.time.LocalDateTime;

public record ContactResponse(
        Long id,
        String name,
        String phone,
        String email,
        String message,
        InquiryStatus status,
        LocalDateTime createdAt
) {}