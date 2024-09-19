package com.example.project_3.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PurchaseRequestDTO {
    private Long productId;
    private Long userId;
    private Integer quantity;
    private BigDecimal totalAmount;
}
