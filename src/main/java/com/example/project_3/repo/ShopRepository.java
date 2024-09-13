package com.example.project_3.repo;

import com.example.project_3.entity.Shop;
import com.example.project_3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    List<Shop> findByApplicationSubmittedTrueAndOpenStatusFalse();
    List<Shop> findByCloseRequestedTrue();
    List<Shop> findByOpenStatusTrue();
    List<Shop> findByOpenStatusFalse();

    Optional<Shop> findByUserId(Long userId);
    List<Shop> findByUser(User user);


}
