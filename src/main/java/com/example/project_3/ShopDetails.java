package com.example.project_3;

import com.example.project_3.entity.Product;
import com.example.project_3.entity.Shop;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ShopDetails {
    private String name;
    private String description;
    private String category;
    private boolean openStatus;
    private List<String> products;

    // Method to convert Shop entity to ShopDetails DTO
    public static ShopDetails fromEntity(Shop shop) {
        ShopDetails details = new ShopDetails();
        details.setName(shop.getName());
        details.setDescription(shop.getDescription());
        details.setCategory(shop.getCategory());
        details.setOpenStatus(shop.isOpenStatus());
        details.setProducts(shop.getProducts().stream().map(Product::getName).collect(Collectors.toList()));
        return details;
    }

    // Getters and setters
}
