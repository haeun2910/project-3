package com.example.project_3.service;

import com.example.project_3.RequestStatus;
import com.example.project_3.dto.PurchaseRequestDTO;
import com.example.project_3.entity.*;
import com.example.project_3.repo.ProductRepository;
import com.example.project_3.repo.PurchaseRequestRepository;
import com.example.project_3.repo.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class PurchaseService {
    private final PurchaseRequestRepository purchaseRequestRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    public PurchaseService(PurchaseRequestRepository purchaseRequestRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.purchaseRequestRepository = purchaseRequestRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }
    public PurchaseRequest createRequest(PurchaseRequestDTO requestDTO) {
        // Ensure the DTO is correctly mapped to the entity
        PurchaseRequest request = new PurchaseRequest();
        request.setProduct(productRepository.findById(requestDTO.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found")));
        request.setUser(userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found")));
        request.setQuantity(requestDTO.getQuantity());
        request.setTotalAmount(requestDTO.getTotalAmount());
        request.setStatus(RequestStatus.PENDING); // Default status

        return purchaseRequestRepository.save(request);
    }




    public void confirmPayment(Long requestId) {
        PurchaseRequest request = purchaseRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));
        // Verify payment
        request.setStatus(RequestStatus.CONFIRMED);
        purchaseRequestRepository.save(request);
    }

    public void acceptRequest(Long requestId) {
        PurchaseRequest request = purchaseRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));
        if (request.getStatus() == RequestStatus.PENDING) {
            // Update stock and set status
            Product product = request.getProduct();
            product.setStock(product.getStock() - request.getQuantity());
            productRepository.save(product);

            request.setStatus(RequestStatus.ACCEPTED);
            purchaseRequestRepository.save(request);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request cannot be accepted");
        }
    }

    public void cancelRequest(Long requestId) {
        PurchaseRequest request = purchaseRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));
        if (request.getStatus() == RequestStatus.PENDING) {
            request.setStatus(RequestStatus.CANCELED);
            purchaseRequestRepository.save(request);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request cannot be canceled");
        }
    }
}



