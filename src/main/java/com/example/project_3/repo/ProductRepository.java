package com.example.project_3.repo;

import com.example.project_3.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Item, Long> {
}
