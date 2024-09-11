package com.example.project_3.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;


import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Payment extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "purchase_request_id")
    @JsonIgnore
    private PurchaseRequest purchaseRequest;

    private BigDecimal amount;
    private boolean confirmed;
}
