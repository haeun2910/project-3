package com.example.project_3.controller;

import com.example.project_3.dto.ProductDetails;
import com.example.project_3.dto.ProductDto;
import com.example.project_3.entity.Product;
import com.example.project_3.entity.Shop;
import com.example.project_3.entity.ShopViewLog;
import com.example.project_3.service.ProductService;
import com.example.project_3.service.ShopService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ProductDetails> addProduct(@RequestBody ProductDto productDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Fetch the shop associated with the provided shopId
        Shop shop = shopService.getShopById(productDto.getShopId());

        if (shop == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Handle this case appropriately
        }

        // Convert ProductDto to Product entity
        Product product = productDto.toProduct();
        product.setShop(shop);

        // Add the product
        Product addedProduct = productService.addProduct(product);

        // Convert added product to ProductDetails DTO
        ProductDetails addedProductDetails = ProductDetails.fromEntity(addedProduct);

        return ResponseEntity.ok(addedProductDetails);
    }


    @PreAuthorize("hasRole('ROLE_BUSINESS')")
    @PutMapping("/update")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Fetch the product by ID
        Product existingProduct = productService.getProductById(productDto.getId());

        // Fetch the shop by ID
        Shop shop = shopService.getShopById(productDto.getShopId());

        if (shop == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Handle this case appropriately
        }

        // Update product details
        existingProduct.setName(productDto.getProductName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setStock(productDto.getStock());
        existingProduct.setShop(shop); // Set the shop for the product

        // Save the updated product
        Product updatedProduct = productService.updateProduct(existingProduct);

        // Convert updated product to ProductDto
        ProductDto updatedProductDto = new ProductDto(updatedProduct);

        return ResponseEntity.ok(updatedProductDto);
    }


    @PreAuthorize("hasRole('ROLE_BUSINESS')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        // Call service method to delete the product
        productService.deleteProduct(id);

        // Return a response indicating successful deletion
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/shop/{shopId}")
    public ResponseEntity<List<Product>> getProductsByShop(@PathVariable Long shopId) {
        List<Product> products = productService.getProductsByShop(shopId);
        return ResponseEntity.ok(products);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_BUSINESS','ROLE_ADMIN')")
    @GetMapping("/search")
    public List<ProductDto> searchProducts(
            @RequestParam String name,
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {
        return productService.searchProducts(name, minPrice, maxPrice);
    }

}
