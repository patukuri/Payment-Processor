package com.example.payment.service;

import com.example.payment.dto.PaymentDto;
import com.example.payment.dto.PaymentResponseDTO;
import com.example.payment.entity.Payment;
import com.example.payment.repository.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class PaymentServiceImpl implements  PaymentService{


    private final PaymentRepository paymentRepository;
    private final StripeService stripeService;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, StripeService stripeService) {
        this.paymentRepository = paymentRepository;
        this.stripeService = stripeService;
    }

    @Override
    public PaymentResponseDTO createPayment(PaymentDto paymentRequestDTO) {
        Payment payment = convertToEntity(paymentRequestDTO);
        Payment savedPayment= paymentRepository.save(payment);
        return convertToResponseDTO(savedPayment);
    }

    private Payment convertToEntity(PaymentDto paymentRequestDTO) {
        Payment payment = new Payment();

        BeanUtils.copyProperties(paymentRequestDTO, payment);
        return payment;
    }

    private PaymentResponseDTO convertToResponseDTO(Payment payment) {
        PaymentResponseDTO responseDTO = new PaymentResponseDTO();
        BeanUtils.copyProperties(payment, responseDTO);
        return responseDTO;
    }


    @Override
    public PaymentResponseDTO processPayment(Long paymentId) throws Exception {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new Exception("Payment not found"));

        try {
            Charge charge = stripeService.chargePayment(
                    payment.getStripeCustomerId(),
                    payment.getBillValue(),
                    "USD"
            );

            payment.setStatus("PAID");
            paymentRepository.save(payment);

            return convertToResponseDTO(payment);
        } catch (StripeException e) {
            payment.setStatus("FAILED");
            paymentRepository.save(payment);
            throw new Exception("Payment processing failed: " + e.getMessage());
        }
    }


}
