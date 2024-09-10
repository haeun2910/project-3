package com.example.project_3.repo;

import com.example.project_3.entity.Product;
import com.example.project_3.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByShop(Shop shop);
}
