package com.bandhuram.backend.service;

import com.bandhuram.backend.dto.ShopImageResponse;
import com.bandhuram.backend.entity.ShopImage;
import com.bandhuram.backend.exception.BadFileException;
import com.bandhuram.backend.exception.ResourceNotFoundException;
import com.bandhuram.backend.repository.ShopImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GalleryService {

    private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/png", "image/webp");

    private final ShopImageRepository shopImageRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Transactional
    public ShopImageResponse upload(MultipartFile file, String caption, Integer sortOrder) {
        if (file == null || file.isEmpty()) {
            throw new BadFileException("No file was uploaded.");
        }
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new BadFileException("Only JPG, PNG, or WEBP images are allowed.");
        }

        try {
            Path dir = Paths.get(uploadDir);
            Files.createDirectories(dir);

            String extension = switch (file.getContentType()) {
                case "image/png" -> ".png";
                case "image/webp" -> ".webp";
                default -> ".jpg";
            };
            String storedName = UUID.randomUUID() + extension;
            Path target = dir.resolve(storedName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            ShopImage saved = shopImageRepository.save(
                    ShopImage.builder()
                            .fileName(storedName)
                            .caption(caption)
                            .sortOrder(sortOrder)
                            .build()
            );
            return toDto(saved);

        } catch (IOException e) {
            throw new BadFileException("Could not save the uploaded image.");
        }
    }

    @Transactional(readOnly = true)
    public List<ShopImageResponse> listAll() {
        return shopImageRepository.findAllByOrderBySortOrderAscUploadedAtDesc()
                .stream().map(this::toDto).toList();
    }

    @Transactional
    public void delete(Long id) {
        ShopImage image = shopImageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found: " + id));

        try {
            Files.deleteIfExists(Paths.get(uploadDir).resolve(image.getFileName()));
        } catch (IOException ignored) {
            // if the file's already gone, still remove the DB row
        }
        shopImageRepository.delete(image);
    }

    private ShopImageResponse toDto(ShopImage image) {
        return new ShopImageResponse(
                image.getId(),
                "/images/shop/" + image.getFileName(),
                image.getCaption(),
                image.getSortOrder(),
                image.getUploadedAt()
        );
    }
}