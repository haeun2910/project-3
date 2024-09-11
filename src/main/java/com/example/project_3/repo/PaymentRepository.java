package com.example.project_3.repo;

import com.example.project_3.entity.Payment;
import com.example.project_3.entity.PurchaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPurchaseRequest(PurchaseRequest purchaseRequest);
}
