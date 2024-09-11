package com.example.project_3.controller;

import com.example.project_3.entity.PurchaseRequest;
import com.example.project_3.service.PurchaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("purchases")
public class PurchaseController {
    private final PurchaseService purchaseService;
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }
    @PostMapping("/request")
    public ResponseEntity<PurchaseRequest> createPurchaseRequest(@RequestParam Long productId,
                                                                 @RequestParam Integer quantity,
                                                                 @RequestParam Long userId) {
        PurchaseRequest request = purchaseService.createPurchaseRequest(productId, quantity, userId);
        return ResponseEntity.ok(request);
    }

    @PostMapping("/accept")
    public ResponseEntity<Void> acceptPurchaseRequest(@RequestParam Long requestId,
                                                      @RequestParam Long currentUserId) {
        purchaseService.acceptPurchaseRequest(requestId, currentUserId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelPurchaseRequest(@RequestParam Long requestId,
                                                      @RequestParam Long currentUserId) {
        purchaseService.cancelPurchaseRequest(requestId, currentUserId);
        return ResponseEntity.ok().build();
    }
}
