package com.ydmins.metapay.payment_service.controller;

import com.ydmins.metapay.payment_service.domain.payment.dto.PaymentRequest;
import com.ydmins.metapay.payment_service.exception.PaymentProcessingException;
import com.ydmins.metapay.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public boolean processPayment(@RequestBody PaymentRequest request){
        System.out.println("requested");
        try{
            return paymentService.processPayment(request);
        } catch (PaymentProcessingException e){
            // 추후 ResponseEntity 추가할 예정
            return false;
        }
    }
}
