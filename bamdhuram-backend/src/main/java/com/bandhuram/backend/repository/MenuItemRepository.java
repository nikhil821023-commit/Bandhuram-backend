package com.bandhuram.backend.repository;

import com.bandhuram.backend.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByCategoryIdOrderBySortOrderAsc(Long categoryId);
}