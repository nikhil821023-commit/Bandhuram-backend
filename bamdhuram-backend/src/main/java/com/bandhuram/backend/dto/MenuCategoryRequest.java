package com.bandhuram.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record MenuCategoryRequest(
        @NotBlank(message = "Category name is required") String name,
        Integer sortOrder
) {}