package com.example.project_3.dto;

import com.example.project_3.entity.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {
    private Long id;
    private String productName;
    private String description;
    private Double price;
    private Integer stock;
    private Long shopId;


    public ProductDto(Product product) {
        this.id = product.getId();
        this.productName = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.shopId = product.getShop() != null ? product.getShop().getId() : null;
    }
    public ProductDto() {
    }

    public Product toProduct() {
        Product product = new Product();
        product.setId(this.id); // Set the ID for update
        product.setName(this.productName);
        product.setDescription(this.description);
        product.setPrice(this.price);
        product.setStock(this.stock);
        // shop will be set in the controller
        return product;
    }
}
