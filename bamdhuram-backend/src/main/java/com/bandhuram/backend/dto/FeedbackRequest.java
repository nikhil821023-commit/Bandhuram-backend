package com.bandhuram.backend.dto;

import jakarta.validation.constraints.*;

public record FeedbackRequest(
        @NotBlank(message = "Name is required") @Size(max = 60) String name,
        @Min(1) @Max(5) int rating,
        @NotBlank(message = "Comment is required") @Size(max = 500) String comment
) {}