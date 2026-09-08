package com.bandhuram.backend.config;

import com.bandhuram.backend.entity.*;
import com.bandhuram.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final AdminUserRepository adminUserRepository;
    private final MenuCategoryRepository categoryRepository;
    private final MenuItemRepository itemRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.default-username}")
    private String defaultAdminUsername;

    @Value("${app.admin.default-password}")
    private String defaultAdminPassword;

    @Override
    public void run(String... args) {
        seedAdmin();
        if (categoryRepository.count() == 0) {
            seedMenu();
        }
    }

    private void seedAdmin() {
        if (adminUserRepository.findByUsername(defaultAdminUsername).isEmpty()) {
            adminUserRepository.save(AdminUser.builder()
                    .username(defaultAdminUsername)
                    .passwordHash(passwordEncoder.encode(defaultAdminPassword))
                    .role("ADMIN")
                    .build());
            System.out.println("Seeded admin user: " + defaultAdminUsername
                    + " — change the password before going live.");
        }
    }

    private void seedMenu() {
        category("Kachori & Snacks", 1, List.of(
                item("Club Kachori (5 pcs)", "₹40"),
                item("Pyaz Kachori", "₹15"),
                item("Kachori", "₹8"),
                item("Hing Kachori", "₹8"),
                item("Khasta Kachori", "₹10"),
                item("Samosa", "₹10"),
                item("Bread Pakora", "₹12"),
                item("Cutlet", "₹15"),
                item("Dhokla", "₹15")
        ));

        category("Chaat Corner", 2, List.of(
                item("Puchka, 6 Flavour (5+1 pcs)", "₹20"),
                item("Suji Puchka (5 pcs)", "₹40"),
                item("Dahi Puchka (6 pcs)", "₹50"),
                item("Papdi Chat", "₹50"),
                item("Kachori Chat", "₹50"),
                item("Aloo Tikki Chat", "₹50"),
                item("Samosa Chat", "₹50")
        ));

        category("Combo", 3, List.of(
                item("Aloo Paratha with Curd (2 pcs)", "₹90"),
                item("Sattu Paratha with Curd (2 pcs)", "₹90"),
                item("Plain Paratha with Curd (2 pcs)", "₹70")
        ));

        category("Thali", 4, List.of(
                item("Bandhuram Thali", "₹130", "Plain rice, 2 roti, dal, sabji, papad & achar")
        ));

        category("Rice & Meals", 5, List.of(
                item("Roti (1 pc)", "₹6"),
                item("Aloo Paratha (1 pc)", "₹40"),
                item("Sattu Paratha (1 pc)", "₹40"),
                item("Plain Paratha (1 pc)", "₹30"),
                item("Plain Rice", "₹50"),
                item("Jeera Rice", "₹60"),
                item("Fried Rice", "₹80"),
                item("Chola Bhatura (Half Plate)", "₹40"),
                item("Chola Bhatura (Full Plate)", "₹70"),
                item("Litti Chokha (2 pcs) — Without Ghee", "₹40"),
                item("Litti Chokha (2 pcs) — With Ghee", "₹50"),
                item("Chola", "₹50 / ₹90"),
                item("Dal Makhani", "₹50 / ₹90"),
                item("Yellow Dal", "₹40 / ₹70"),
                item("Mix Veg", "₹90"),
                item("Aloo Dum", "₹70")
        ));

        category("South Indian", 6, List.of(
                item("Idli (2 pcs)", "₹30"),
                item("Sambar Vada (2 pcs)", "₹40"),
                item("Dahi Vada (2 pcs)", "₹40")
        ));

        category("Sweets", 7, List.of(
                item("Kesharia Jalebi (1 pc)", "₹10"),
                item("Gulab Jamun (1 pc)", "₹15"),
                item("Rasgulla (1 pc)", "₹10")
        ));

        category("Beverages", 8, List.of(
                item("Tea", "₹10 / ₹15 / ₹20"),
                item("Keshar Tea", "₹20"),
                item("Lassi", "₹50"),
                item("Soda Sikanji", "₹40"),
                item("Masala Cold Drinks", "₹40"),
                item("Cold Drink", "MRP"),
                item("Water 500 ml", "MRP"),
                item("Water 1 L", "MRP")
        ));

        category("Healthy Add-ons", 9, List.of(
                item("Fresh Salad", "₹40"),
                item("Papad", "₹10")
        ));

        System.out.println("Seeded full Bandhuram menu.");
    }

    private void category(String name, int sortOrder, List<MenuItem> items) {
        MenuCategory category = categoryRepository.save(
                MenuCategory.builder().name(name).sortOrder(sortOrder).build()
        );
        int i = 1;
        for (MenuItem item : items) {
            item.setCategory(category);
            item.setSortOrder(i++);
            itemRepository.save(item);
        }
    }

    private MenuItem item(String name, String priceLabel) {
        return MenuItem.builder().name(name).priceLabel(priceLabel).available(true).build();
    }

    private MenuItem item(String name, String priceLabel, String description) {
        return MenuItem.builder().name(name).priceLabel(priceLabel).description(description).available(true).build();
    }
}