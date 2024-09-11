package com.example.project_3.controller;

import com.example.project_3.entity.Product;
import com.example.project_3.entity.ShopViewLog;
import com.example.project_3.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PreAuthorize("hasRole('ROLE_BUSINESS')")
    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(@RequestParam Long shopId, @RequestBody Product product) {
        Product addedProduct = productService.addProduct(shopId, product);
        return ResponseEntity.ok(addedProduct);
    }

    @PreAuthorize("hasRole('ROLE_BUSINESS')")
    @PutMapping("/update/{productId}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long productId,
            @RequestParam Long shopId,
            @RequestBody Product product) {
        try {
            Product updatedProduct = productService.updateProduct(productId, shopId, product);
            return ResponseEntity.ok(updatedProduct);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(null);
        }
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
