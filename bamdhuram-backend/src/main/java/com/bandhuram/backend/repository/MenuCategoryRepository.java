package com.bandhuram.backend.repository;

import com.bandhuram.backend.entity.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Long> {
    List<MenuCategory> findAllByOrderBySortOrderAsc();
    Optional<MenuCategory> findByNameIgnoreCase(String name);
}