package com.example.project_3.repo;

import com.example.project_3.entity.Product;
import com.example.project_3.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByShop(Shop shop);
    // Search by name, price range, shop's active owner, open status, and application submission
    @Query("SELECT p FROM Product p WHERE " +
            "(p.name LIKE %:name% OR :name IS NULL) AND " +
            "p.price BETWEEN :minPrice AND :maxPrice AND " +
            "p.shop.user.active = true AND " +
            "p.shop.openStatus = true AND " +
            "p.shop.applicationSubmitted = true")
    List<Product> findByNameContainingAndPriceBetween(String name, Double minPrice, Double maxPrice);
}
