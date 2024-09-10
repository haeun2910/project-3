package com.example.project_3.repo;

import com.example.project_3.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    List<Shop> findByApplicationSubmittedTrueAndOpenStatusFalse();
    List<Shop> findByCloseRequestedTrue();
    @Query("SELECT s FROM Shop s WHERE s.owner.active = true ORDER BY s.recentTransaction DESC")
    List<Shop> findAllByOwnerActiveTrueOrderByRecentTransactionDesc();
    @Query("SELECT s FROM Shop s")
    List<Shop> findAll();


    // Search shops by name and category, and owner must be active
    List<Shop> findByNameContainingAndCategoryContainingAndOwnerActiveTrue(String name, String category);

    // Search shops by name, and owner must be active
    List<Shop> findByNameContainingAndOwnerActiveTrue(String name);

    // Search shops by category, and owner must be active
    List<Shop> findByCategoryContainingAndOwnerActiveTrue(String category);
}
