package com.bandhuram.backend.dto;

import java.util.List;

public record MenuCategoryDto(
        Long id,
        String name,
        Integer sortOrder,
        List<MenuItemDto> items
) {}