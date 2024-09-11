package com.example.project_3.controller;

import com.example.project_3.entity.Payment;
import com.example.project_3.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("payments")
public class PaymentController {
    private final PaymentService paymentService;
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    @PostMapping("/submit")
    public ResponseEntity<Payment> submitPayment(@RequestParam Long requestId,
                                                 @RequestParam BigDecimal amount) {
        Payment payment = paymentService.submitPayment(requestId, amount);
        return ResponseEntity.ok(payment);
    }

    @PostMapping("/confirm")
    public ResponseEntity<Void> confirmPayment(@RequestParam Long paymentId) {
        paymentService.confirmPayment(paymentId);
        return ResponseEntity.ok().build();
    }
}
