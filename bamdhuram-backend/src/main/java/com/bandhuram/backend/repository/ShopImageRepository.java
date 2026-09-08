package com.bandhuram.backend.repository;

import com.bandhuram.backend.entity.ShopImage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ShopImageRepository extends JpaRepository<ShopImage, Long> {
    List<ShopImage> findAllByOrderBySortOrderAscUploadedAtDesc();
}