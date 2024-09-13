package com.example.project_3.controller;

import com.example.project_3.ProductDetails;
import com.example.project_3.entity.Product;
import com.example.project_3.entity.Shop;
import com.example.project_3.entity.ShopViewLog;
import com.example.project_3.service.ProductService;
import com.example.project_3.service.ShopService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("products")
public class ProductController {
    private final ProductService productService;
    private final ShopService shopService;

    public ProductController(ProductService productService, ShopService shopService) {
        this.productService = productService;
        this.shopService = shopService;
    }

    @PreAuthorize("hasRole('ROLE_BUSINESS')")
    @PostMapping("/add")
    public ResponseEntity<ProductDetails> addProduct(@RequestBody ProductDetails productDetails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Convert ProductDetails to Product entity
        Product product = new Product();
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setImage(productDetails.getImage());

        // Fetch the shop associated with the current user
        Shop shop = shopService.getShopByCurrentUser();

        // Set the shop in the product
        product.setShop(shop);

        // Add the product
        Product addedProduct = productService.addProduct(shop.getId(), product);

        // Convert added product to ProductDetails DTO
        ProductDetails addedProductDetails = ProductDetails.fromEntity(addedProduct);

        return ResponseEntity.ok(addedProductDetails);
    }


    @PreAuthorize("hasRole('ROLE_BUSINESS')")
    @PutMapping("/update/{productId}")
    public ResponseEntity<ProductDetails> updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductDetails productDetails) {

        // Get the current authenticated username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Fetch the shop associated with the current user
        Shop shop = shopService.getShopByCurrentUser();

        // Convert ProductDetails to Product entity
        Product updatedProduct = new Product();
        updatedProduct.setName(productDetails.getName());
        updatedProduct.setDescription(productDetails.getDescription());
        updatedProduct.setPrice(productDetails.getPrice());
        updatedProduct.setImage(productDetails.getImage());
        updatedProduct.setStock(productDetails.getStock());

        // Update the product
        Product product = productService.updateProduct(productId, shop.getId(), updatedProduct);

        // Convert updated product to ProductDetails DTO
        ProductDetails updatedProductDetails = ProductDetails.fromEntity(product);

        return ResponseEntity.ok(updatedProductDetails);
    }


    @PreAuthorize("hasRole('ROLE_BUSINESS')")
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/shop/{shopId}")
    public ResponseEntity<List<Product>> getProductsByShop(@PathVariable Long shopId) {
        List<Product> products = productService.getProductsByShop(shopId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam Long userId) {
        List<Product> products = productService.searchProducts(name, minPrice, maxPrice, userId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/recent-shops")
    public ResponseEntity<List<ShopViewLog>> getRecentShopViews(@RequestParam Long userId) {
        List<ShopViewLog> logs = productService.getRecentShopViews(userId);
        return ResponseEntity.ok(logs);
    }
}
