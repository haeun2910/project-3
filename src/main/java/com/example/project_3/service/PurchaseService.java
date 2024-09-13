package com.example.project_3.service;

import com.example.project_3.entity.*;
import com.example.project_3.repo.PaymentRepository;
import com.example.project_3.repo.ProductRepository;
import com.example.project_3.repo.PurchaseRequestRepository;
import com.example.project_3.repo.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@Builder
public class PurchaseService {
    private final PurchaseRequestRepository purchaseRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    public PurchaseService(PurchaseRequestRepository purchaseRepository, ProductRepository productRepository, UserRepository userRepository, PaymentRepository paymentRepository) {
        this.purchaseRepository = purchaseRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
    }

    public PurchaseRequest createPurchaseRequest(Long productId, Integer quantity, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not active");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        if (product.getStock() < quantity) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock");
        }

        // Initialize lazy-loaded properties
        product.getShop().getUser(); // Ensure initialization of owner

        PurchaseRequest request = PurchaseRequest.builder()
                .product(product)
                .user(user)
                .quantity(quantity)
                .isAccepted(false)
                .isCancelled(false)
                .build();

        return purchaseRepository.save(request);
    }

    public void acceptPurchaseRequest(Long requestId, Long currentUserId) {
        PurchaseRequest request = purchaseRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase request not found"));

        if (request.isCancelled()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request has been cancelled");
        }

        if (request.isAccepted()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request has already been accepted");
        }

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!currentUser.getAuthorities().contains("ROLE_BUSINESS")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only shop owners can accept the request");
        }

        Shop shop = request.getProduct().getShop();
        if (!shop.getUser().equals(currentUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the shop owner can accept the request");
        }

        // Check if payment has been confirmed
        Payment payment = paymentRepository.findByPurchaseRequest(request)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No payment found for this request"));

        if (!payment.isConfirmed()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment has not been confirmed");
        }

        // Update stock
        Product product = request.getProduct();
        product.setStock(product.getStock() - request.getQuantity());
        productRepository.save(product);

        // Mark request as accepted
        request.setAccepted(true);
        purchaseRepository.save(request);
    }

    public void cancelPurchaseRequest(Long requestId, Long currentUserId) {
        PurchaseRequest request = purchaseRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase request not found"));

        if (request.isAccepted()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request cannot be cancelled after acceptance");
        }

        // Check if the current user is a shop owner
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!currentUser.getAuthorities().contains("ROLE_BUSINESS")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only shop owners can cancel the request");
        }

        Shop shop = request.getProduct().getShop();
        if (!shop.getUser().equals(currentUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the shop owner can cancel the request");
        }

        // Mark request as cancelled
        request.setCancelled(true);
        purchaseRepository.save(request);
    }
}


