package com.example.project_3.service;

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
    public Product addProduct(Long shopId, Product product) {
        Optional<Shop> shopOptional = shopRepository.findById(shopId);
        if (!shopOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shop not found");
        }
        Shop shop = shopOptional.get();
        product.setShop(shop);

        return productRepository.save(product);
    }

    public Product updateProduct(Long productId, Long shopId, Product updatedProduct) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            // Check if the product belongs to the given shop
            if (!product.getShop().getId().equals(shopId)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Product does not belong to the specified shop");
            }

            // Update product details
            product.setName(updatedProduct.getName());
            product.setImage(updatedProduct.getImage());
            product.setDescription(updatedProduct.getDescription());
            product.setPrice(updatedProduct.getPrice());
            product.setStock(updatedProduct.getStock());

            return productRepository.save(product);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
    }


    public void deleteProduct(Long productId) {
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
    }

    public List<Product> getProductsByShop(Long shopId) {
        Optional<Shop> shopOptional = shopRepository.findById(shopId);
        if (shopOptional.isPresent()) {
            return productRepository.findByShop(shopOptional.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shop not found");
        }
    }

    public List<Product> searchProducts(String name, BigDecimal minPrice, BigDecimal maxPrice, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if ( !user.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not active");
        }
        List<Product> products = productRepository.findByNameContainingAndPriceBetweenAndShopUserActiveTrueAndShopOpenStatusTrueAndShopApplicationSubmittedTrue(name,minPrice,maxPrice);
        products.forEach(product -> logShopView(product.getShop(), user));
        return products;
    }

    private void logShopView(Shop shop, User user) {
        ShopViewLog log = new ShopViewLog();
        log.setShop(shop);
        log.setUser(user);
        log.setViewedAt(LocalDateTime.now());
        shopViewRepository.save(log);
    }

    public List<ShopViewLog> getRecentShopViews(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return shopViewRepository.findTop5ByUserOrderByViewedAtDesc(user);
    }

}
