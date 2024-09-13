package com.example.project_3;

import com.example.project_3.entity.Shop;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShopDetails {
    private String name;
    private String description;
    private String category;
    private boolean openStatus;

    // Method to convert Shop entity to ShopDetails DTO
    public static ShopDetails fromEntity(Shop shop) {
        ShopDetails details = new ShopDetails();
        details.setName(shop.getName());
        details.setDescription(shop.getDescription());
        details.setCategory(shop.getCategory());
        details.setOpenStatus(shop.isOpenStatus());
        return details;
    }

    // Getters and setters
}
