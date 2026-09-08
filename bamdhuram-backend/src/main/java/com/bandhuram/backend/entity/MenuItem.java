package com.bandhuram.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "menu_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private MenuCategory category;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(length = 300)
    private String description;

    // kept as a label rather than a number — the real menu has values
    // like "₹50 / ₹90", "MRP", "₹10 / ₹15 / ₹20"
    @Column(name = "price_label", nullable = false, length = 40)
    private String priceLabel;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(nullable = false)
    @Builder.Default
    private boolean available = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean featured = false;

    @Column(name = "photo_url", length=300)
    private String photoUrl;
}