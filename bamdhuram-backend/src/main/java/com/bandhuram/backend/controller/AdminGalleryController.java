package com.bandhuram.backend.controller;

import com.bandhuram.backend.dto.ShopImageResponse;
import com.bandhuram.backend.service.GalleryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/gallery")
@RequiredArgsConstructor
public class AdminGalleryController {

    private final GalleryService galleryService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ShopImageResponse> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "caption", required = false) String caption,
            @RequestParam(value = "sortOrder", required = false) Integer sortOrder
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(galleryService.upload(file, caption, sortOrder));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        galleryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}