package com.example.project_3.repo;

import com.example.project_3.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    List<Shop> findByApplicationSubmittedTrueAndOpenStatusFalse();
    List<Shop> findByCloseRequestedTrue();
}
