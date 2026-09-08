package com.bandhuram.backend.controller;

import com.bandhuram.backend.dto.*;
import com.bandhuram.backend.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/menu")
@RequiredArgsConstructor
public class AdminMenuController {

    private final MenuService menuService;

    @PostMapping("/categories")
    public ResponseEntity<MenuCategoryDto> createCategory(@Valid @RequestBody MenuCategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(menuService.createCategory(request));
    }

    @PutMapping("/categories/{id}")
    public MenuCategoryDto updateCategory(@PathVariable Long id, @Valid @RequestBody MenuCategoryRequest request) {
        return menuService.updateCategory(id, request);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        menuService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/items")
    public ResponseEntity<MenuItemDto> createItem(@Valid @RequestBody MenuItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(menuService.createItem(request));
    }

    @PutMapping("/items/{id}")
    public MenuItemDto updateItem(@PathVariable Long id, @Valid @RequestBody MenuItemRequest request) {
        return menuService.updateItem(id, request);
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        menuService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    // add to AdminMenuController.java

    @PostMapping(value = "/items/{id}/photo", consumes = "multipart/form-data")
    public MenuItemDto uploadItemPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return menuService.uploadItemPhoto(id, file);
    }
}