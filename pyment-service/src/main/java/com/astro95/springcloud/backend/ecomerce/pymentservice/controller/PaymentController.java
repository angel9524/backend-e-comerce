package com.astro95.springcloud.backend.ecomerce.pymentservice.controller;

import com.astro95.springcloud.backend.ecomerce.pymentservice.service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.InvoicePayment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<?> createPayment(@RequestParam double amount, @RequestParam String currency) throws Exception {
        try {
            Map<String, Object> respone = paymentService.createPaymentIntent(amount, currency);
            return ResponseEntity.ok(respone);
        } catch (StripeException e) {
            throw new Exception(e.getMessage());
        }
    }
}
