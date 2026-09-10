package com.bandhuram.backend.service;

import com.bandhuram.backend.dto.ShopImageResponse;
import com.bandhuram.backend.entity.ShopImage;
import com.bandhuram.backend.exception.BadFileException;
import com.bandhuram.backend.exception.ResourceNotFoundException;
import com.bandhuram.backend.repository.ShopImageRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GalleryService {

    private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/png", "image/webp");

    private final ShopImageRepository shopImageRepository;
    private final Cloudinary cloudinary;

    @Transactional
    public ShopImageResponse upload(MultipartFile file, String caption, Integer sortOrder) {
        if (file == null || file.isEmpty()) {
            throw new BadFileException("No file was uploaded.");
        }
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new BadFileException("Only JPG, PNG, or WEBP images are allowed.");
        }

        try {
            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "bandhuram/shop",
                    "resource_type", "image"
            ));
            String publicId = (String) result.get("public_id");
            String secureUrl = (String) result.get("secure_url");

            ShopImage saved = shopImageRepository.save(
                    ShopImage.builder()
                            .fileName(publicId)
                            .imageUrl(secureUrl)
                            .caption(caption)
                            .sortOrder(sortOrder)
                            .build()
            );
            return toDto(saved);

        } catch (IOException e) {
            throw new BadFileException("Could not upload the image.");
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
            cloudinary.uploader().destroy(image.getFileName(), ObjectUtils.emptyMap());
        } catch (IOException ignored) {
            // if it's already gone on Cloudinary's side, still remove the DB row
        }
        shopImageRepository.delete(image);
    }

    private ShopImageResponse toDto(ShopImage image) {
        return new ShopImageResponse(
                image.getId(),
                image.getImageUrl(),
                image.getCaption(),
                image.getSortOrder(),
                image.getUploadedAt()
        );
    }
}