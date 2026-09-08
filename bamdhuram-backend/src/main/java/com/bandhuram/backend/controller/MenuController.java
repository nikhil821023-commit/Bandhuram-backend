package com.bandhuram.backend.controller;

import com.bandhuram.backend.dto.MenuCategoryDto;
import com.bandhuram.backend.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public List<MenuCategoryDto> getFullMenu() {
        return menuService.getFullMenu();
    }
}