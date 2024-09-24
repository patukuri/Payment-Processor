package com.example.payment.controller;

import com.example.payment.dto.PaymentDto;
import com.example.payment.dto.PaymentResponseDTO;
import com.example.payment.exception.PaymentProcessingException;
import com.example.payment.service.PaymentService;
import com.example.payment.service.PaymentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentServiceImpl paymentService;


    @PostMapping("/create")
    public ResponseEntity<PaymentResponseDTO> createPayment(@RequestBody PaymentDto paymentRequestDTO) {
        PaymentResponseDTO createdPayment = paymentService.createPayment(paymentRequestDTO);
        return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
    }

    @PostMapping("/{paymentId}/process")
    public ResponseEntity<PaymentResponseDTO> processPayment(@PathVariable Long paymentId) {
        try {
            PaymentResponseDTO response = paymentService.processPayment(paymentId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new PaymentResponseDTO(null, "ERROR", "An unexpected error occurred"));
        }
    }
}