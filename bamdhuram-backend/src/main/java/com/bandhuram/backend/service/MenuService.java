package com.bandhuram.backend.service;

import com.bandhuram.backend.dto.*;
import com.bandhuram.backend.entity.MenuCategory;
import com.bandhuram.backend.entity.MenuItem;
import com.bandhuram.backend.exception.BadFileException;
import com.bandhuram.backend.exception.ResourceNotFoundException;
import com.bandhuram.backend.repository.MenuCategoryRepository;
import com.bandhuram.backend.repository.MenuItemRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuCategoryRepository categoryRepository;
    private final MenuItemRepository itemRepository;
    private final Cloudinary cloudinary;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Transactional(readOnly = true)
    public List<MenuCategoryDto> getFullMenu() {
        return categoryRepository.findAllByOrderBySortOrderAsc().stream()
                .map(this::toDto)
                .toList();
    }

    private MenuCategoryDto toDto(MenuCategory category) {
        List<MenuItemDto> items = category.getItems().stream()
                .sorted((a, b) -> {
                    int soA = a.getSortOrder() == null ? 0 : a.getSortOrder();
                    int soB = b.getSortOrder() == null ? 0 : b.getSortOrder();
                    return Integer.compare(soA, soB);
                })
                .map(this::toDto)
                .toList();
        return new MenuCategoryDto(category.getId(), category.getName(), category.getSortOrder(), items);
    }

    private MenuItemDto toDto(MenuItem i) {
        return new MenuItemDto(i.getId(), i.getName(), i.getDescription(), i.getPriceLabel(),
                i.getSortOrder(), i.isAvailable(), i.isFeatured(), i.getPhotoUrl());
    }

    // ---- admin operations ----

    @Transactional
    public MenuCategoryDto createCategory(MenuCategoryRequest req) {
        MenuCategory saved = categoryRepository.save(
                MenuCategory.builder().name(req.name()).sortOrder(req.sortOrder()).build()
        );
        return toDto(saved);
    }

    @Transactional
    public MenuCategoryDto updateCategory(Long id, MenuCategoryRequest req) {
        MenuCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
        category.setName(req.name());
        category.setSortOrder(req.sortOrder());
        return toDto(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found: " + id);
        }
        categoryRepository.deleteById(id);
    }

    @Transactional
    public MenuItemDto createItem(MenuItemRequest req) {
        MenuCategory category = categoryRepository.findById(req.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + req.categoryId()));

        MenuItem item = MenuItem.builder()
                .category(category)
                .name(req.name())
                .description(req.description())
                .priceLabel(req.priceLabel())
                .sortOrder(req.sortOrder())
                .available(req.available() == null || req.available())
                .featured(req.featured() != null && req.featured())
                .build();

        MenuItem saved = itemRepository.save(item);
        return toDto(saved);
    }

    @Transactional
    public MenuItemDto updateItem(Long id, MenuItemRequest req) {
        MenuItem item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found: " + id));

        if (!item.getCategory().getId().equals(req.categoryId())) {
            MenuCategory category = categoryRepository.findById(req.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + req.categoryId()));
            item.setCategory(category);
        }

        item.setName(req.name());
        item.setDescription(req.description());
        item.setPriceLabel(req.priceLabel());
        item.setSortOrder(req.sortOrder());
        if (req.available() != null) item.setAvailable(req.available());
        if (req.featured() != null) item.setFeatured(req.featured());

        return toDto(item);
    }

    @Transactional
    public MenuItemDto uploadItemPhoto(Long id, MultipartFile file) {
        MenuItem item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found: " + id));

        if (file == null || file.isEmpty()) {
            throw new BadFileException("No file was uploaded.");
        }
        if (!Set.of("image/jpeg", "image/png", "image/webp").contains(file.getContentType())) {
            throw new BadFileException("Only JPG, PNG, or WEBP images are allowed.");
        }

        try {
            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "bandhuram/menu-items",
                    "resource_type", "image"
            ));
            item.setPhotoUrl((String) result.get("secure_url"));
            item.setPhotoPublicId((String) result.get("public_id"));
            return toDto(item);

        } catch (IOException e) {
            throw new BadFileException("Could not upload the image.");
        }
    }




    @Transactional
    public void deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Item not found: " + id);
        }
        itemRepository.deleteById(id);
    }


}