package com.bandhuram.backend.dto;

import jakarta.validation.constraints.*;

public record ContactRequest(
        @NotBlank(message = "Name is required") @Size(max = 60) String name,
        @NotBlank(message = "Phone is required") @Size(max = 20) String phone,
        @Email(message = "Enter a valid email") String email,
        @NotBlank(message = "Message is required") @Size(max = 500) String message
) {}