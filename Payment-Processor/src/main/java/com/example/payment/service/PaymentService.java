package com.example.payment.service;

import com.example.payment.dto.PaymentDto;
import com.example.payment.dto.PaymentResponseDTO;

public interface PaymentService {

    PaymentResponseDTO createPayment(PaymentDto paymentRequestDTO);

    PaymentResponseDTO processPayment(Long paymentId) throws Exception;
}
