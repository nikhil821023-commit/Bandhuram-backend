package com.bandhuram.backend.controller;

import com.bandhuram.backend.dto.ShopImageResponse;
import com.bandhuram.backend.service.GalleryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gallery")
@RequiredArgsConstructor
public class GalleryController {

    private final GalleryService galleryService;

    @GetMapping
    public List<ShopImageResponse> list() {
        return galleryService.listAll();
    }
}