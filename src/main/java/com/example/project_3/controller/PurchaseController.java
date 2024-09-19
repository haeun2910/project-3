package com.example.project_3.controller;

import com.example.project_3.dto.PurchaseRequestDTO;
import com.example.project_3.entity.PurchaseRequest;
import com.example.project_3.service.PurchaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("purchases")
public class PurchaseController {
    private final PurchaseService purchaseService;
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }
    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_BUSINESS','ROLE_ADMIN')")
    public ResponseEntity<PurchaseRequest> createRequest(@RequestBody PurchaseRequestDTO requestDTO) {
        try {
            // Make sure these methods exist in your service
            PurchaseRequest request = purchaseService.createRequest(requestDTO);
            return new ResponseEntity<>(request, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    // Endpoint to confirm payment for a purchase request
    @PutMapping("/confirm/{requestId}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_BUSINESS','ROLE_ADMIN')")
    public ResponseEntity<Void> confirmPayment(@PathVariable Long requestId) {
        try {
            purchaseService.confirmPayment(requestId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint to accept a purchase request (Shop Owner role)
    @PutMapping("/accept/{requestId}")
    @PreAuthorize("hasRole('ROLE_BUSINESS')")
    public ResponseEntity<Void> acceptRequest(@PathVariable Long requestId) {
        try {
            purchaseService.acceptRequest(requestId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint to cancel a purchase request
    @DeleteMapping("/cancel/{requestId}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_BUSINESS','ROLE_ADMIN')")
    public ResponseEntity<Void> cancelRequest(@PathVariable Long requestId) {
        try {
            purchaseService.cancelRequest(requestId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
