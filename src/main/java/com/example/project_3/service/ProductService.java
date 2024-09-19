package com.example.project_3.service;

import com.example.project_3.dto.ProductDto;
import com.example.project_3.entity.Product;
import com.example.project_3.entity.Shop;
import com.example.project_3.entity.ShopViewLog;
import com.example.project_3.entity.User;
import com.example.project_3.repo.ProductRepository;
import com.example.project_3.repo.ShopRepository;
import com.example.project_3.repo.ShopViewRepository;
import com.example.project_3.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;
    private final ShopViewRepository shopViewRepository;
    public ProductService(ProductRepository productRepository, ShopRepository shopRepository, UserRepository userRepository, ShopViewRepository shopViewRepository) {
        this.productRepository = productRepository;
        this.shopRepository = shopRepository;
        this.userRepository = userRepository;
        this.shopViewRepository = shopViewRepository;
    }
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    public Product updateProduct(Product product) {
        // Ensure the product exists before updating
        if (!productRepository.existsById(product.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        return productRepository.save(product);
    }


    public void deleteProduct(Long id) {
        // Check if the product exists
        if (!productRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        // Delete the product
        productRepository.deleteById(id);
    }

    public List<Product> getProductsByShop(Long shopId) {
        Optional<Shop> shopOptional = shopRepository.findById(shopId);
        if (shopOptional.isPresent()) {
            return productRepository.findByShop(shopOptional.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shop not found");
        }
    }

    public List<ProductDto> searchProducts(String name, Double minPrice, Double maxPrice) {
        List<Product> products = productRepository.findByNameContainingAndPriceBetween(name, minPrice, maxPrice);
        return products.stream()
                .map(ProductDto::new)
                .collect(Collectors.toList());
    }


}
