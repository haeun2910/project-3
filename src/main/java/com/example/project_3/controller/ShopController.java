package com.example.project_3.controller;

import com.example.project_3.entity.Shop;
import com.example.project_3.service.ShopService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/shops")
public class ShopController {
    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;

    }
    @PreAuthorize("hasAnyRole('ROLE_BUSINESS','ROLE_ADMIN')")
    @GetMapping("all-shops")
    public List<Shop> getAllShops() {
        return shopService.getAllShops();
    }

    @GetMapping("opened-shop")
    public ResponseEntity<List<Shop>> getOpenedShops() {
        List<Shop> shops = shopService.getAllOpenedShops();
        return ResponseEntity.ok(shops);
    }
    @GetMapping("get-shop/{id}")
    public Shop getShopById(@PathVariable Long id) {
        return shopService.getShopById(id);
    }

    @PreAuthorize("hasRole('ROLE_BUSINESS')")
    @PutMapping("/update")
    public ResponseEntity<Shop> updateShop(@RequestParam Long userId, @RequestBody Shop shop) {
        Shop updatedShop = shopService.updateShopForBusinessUser(userId, shop);
        if (updatedShop != null) {
            return ResponseEntity.ok(updatedShop);
        } else {
            // If shop is not found or user is not authorized, return a different status
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @PreAuthorize("hasRole('ROLE_BUSINESS')")
    @PostMapping("open-apply")
    public ResponseEntity<?> createShop(@RequestParam Long userId, @RequestBody Shop shop) {
        try {
            Shop openApply = shopService.openApply(userId, shop);
            return ResponseEntity.ok(openApply);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());  // Return the specific error status and message
        } catch (Exception e) {
            // Handle other unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/application-shop")
    public ResponseEntity<List<Shop>> getApplicationShops() {
        return ResponseEntity.ok(shopService.getApplicationShops());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/approve/{shopId}")
    public ResponseEntity<String> approveShop(@PathVariable Long shopId) {
        try {
            shopService.approveShop(shopId);
            return ResponseEntity.ok("Shop approved");
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/reject/{shopId}")
    public ResponseEntity<String> rejectShop(@PathVariable Long shopId, @RequestParam String reason) {
        try {
            shopService.rejectShop(shopId, reason);
            return ResponseEntity.ok("Shop rejected with reason: " + reason);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }
    @PostMapping("/close-request")
    @PreAuthorize("hasRole('ROLE_BUSINESS')")
    public ResponseEntity<String> requestShopClose(@RequestParam Long shopId, @RequestParam String reason) {
        try {
            shopService.requestShopClose(shopId, reason);
            return ResponseEntity.ok("Shop closure request submitted with reason: " + reason);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/close-requests")
    public ResponseEntity<List<Shop>> getCloseRequests() {
        List<Shop> closeRequestedShops = shopService.getShopsWithCloseRequests();
        return ResponseEntity.ok(closeRequestedShops);
    }
    @PostMapping("/approve-close/{shopId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> approveShopClosure(@PathVariable Long shopId) {
        try {
            shopService.approveShopClose(shopId);
            return ResponseEntity.ok("Shop closure approved.");
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }

}
