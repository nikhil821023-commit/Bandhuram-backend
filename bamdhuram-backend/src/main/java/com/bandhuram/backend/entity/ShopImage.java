package com.bandhuram.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shop_images")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ShopImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false, length = 200)
    private String fileName;       // stored filename on disk (unique)

    @Column(length = 150)
    private String caption;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "uploaded_at", nullable = false)
    @Builder.Default
    private LocalDateTime uploadedAt = LocalDateTime.now();

    @Column(name = "image_url", length = 500)
    private String imageUrl;   // full Cloudinary secure_url — served directly, no local path needed
}