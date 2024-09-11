package com.example.project_3.service;

import com.example.project_3.entity.Payment;
import com.example.project_3.entity.PurchaseRequest;
import com.example.project_3.repo.PaymentRepository;
import com.example.project_3.repo.PurchaseRequestRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PurchaseRequestRepository purchaseRequestRepository;
    public PaymentService(PaymentRepository paymentRepository, PurchaseRequestRepository purchaseRequestRepository) {
        this.paymentRepository = paymentRepository;
        this.purchaseRequestRepository = purchaseRequestRepository;
    }
    public Payment submitPayment(Long requestId, BigDecimal amount) {
        PurchaseRequest request = purchaseRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase request not found"));

        if (request.isAccepted() || request.isCancelled()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot submit payment for an accepted or cancelled request");
        }

        Payment payment = Payment.builder()
                .purchaseRequest(request)
                .amount(amount)
                .confirmed(false)
                .build();

        return paymentRepository.save(payment);
    }

    public void confirmPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));

        if (payment.isConfirmed()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment is already confirmed");
        }

        PurchaseRequest request = payment.getPurchaseRequest();

        if (request.getProduct().getPrice().compareTo(payment.getAmount()) != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment amount does not match the product price");
        }

        payment.setConfirmed(true);
        paymentRepository.save(payment);
    }
}
