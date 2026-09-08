package com.bandhuram.backend.dto;

import java.time.LocalDateTime;

public record FeedbackResponse(
        Long id,
        String name,
        int rating,
        String comment,
        LocalDateTime createdAt
) {}