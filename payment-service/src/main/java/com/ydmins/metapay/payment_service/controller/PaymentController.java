package com.ydmins.metapay.payment_service.controller;

import com.ydmins.metapay.payment_service.common.ApiResponse;
import com.ydmins.metapay.payment_service.common.PaymentMessages;
import com.ydmins.metapay.payment_service.domain.payment.dto.GetPaymentResponse;
import com.ydmins.metapay.payment_service.domain.payment.dto.PaymentRequest;
import com.ydmins.metapay.payment_service.exception.PaymentNotFoundException;
import com.ydmins.metapay.payment_service.exception.PaymentPersistenceException;
import com.ydmins.metapay.payment_service.exception.PaymentProcessingException;
import com.ydmins.metapay.payment_service.exception.PaymentServiceException;
import com.ydmins.metapay.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.ydmins.metapay.payment_service.common.PaymentMessages.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public boolean processPayment(@RequestBody PaymentRequest request){
        log.info(PAYMENT_SUCCESS);
        try{
            return paymentService.processPayment(request);
        } catch (PaymentProcessingException e){
            // 추후 ResponseEntity 추가할 예정
            return false;
        }
    }

    @GetMapping("/{paymentId}")
    public ApiResponse<?> getPayment(@PathVariable  Long paymentId){
        log.info("Retrieving payment status for paymentId : {}", paymentId);
        GetPaymentResponse response =  paymentService.getPaymentDetail(paymentId);
        return ApiResponse.success(200, PAYMENT_DETAIL_RETRIEVED, response);
    }
}
