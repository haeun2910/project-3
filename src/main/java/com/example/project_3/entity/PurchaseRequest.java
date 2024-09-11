package com.example.project_3.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PurchaseRequest extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Integer quantity;
    private BigDecimal totalPrice;
    private boolean isAccepted;
    private boolean isCancelled;

    @PrePersist
    protected void onCreate() {
        // Calculate totalPrice based on quantity and product price
        if (product != null && quantity != null) {
            this.totalPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        }
    }
}
