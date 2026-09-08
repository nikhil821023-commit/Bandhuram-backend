package com.bandhuram.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // reference only — kept nullable in case the menu item is later deleted
    @Column(name = "menu_item_id")
    private Long menuItemId;

    // snapshotted at order time so edits to the live menu never rewrite past orders
    @Column(name = "item_name", nullable = false, length = 120)
    private String itemName;

    @Column(name = "price_label", nullable = false, length = 40)
    private String priceLabel;

    @Column(nullable = false)
    private int quantity;
}