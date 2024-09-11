package com.example.project_3.repo;

import com.example.project_3.entity.ShopViewLog;
import com.example.project_3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopViewRepository extends JpaRepository<ShopViewLog, Long> {
    List<ShopViewLog> findTop5ByUserOrderByViewedAtDesc(User user);
}
