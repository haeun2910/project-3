package com.example.project_3.dto;

import com.example.project_3.entity.Product;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductDetails {
    private String name;
    private String description;
    private BigDecimal price;
    private String image;
    private Integer stock;
    private Long shopId;

    public static ProductDetails fromEntity(Product product) {
        ProductDetails details = new ProductDetails();
        details.setName(product.getName());
        details.setDescription(product.getDescription());
        details.setPrice(BigDecimal.valueOf(product.getPrice() != null ? product.getPrice().doubleValue() : null));
        details.setImage(product.getImage());
        details.setStock(product.getStock());
        details.setShopId(product.getShop() != null ? product.getShop().getId() : null);
        return details;
    }


}
