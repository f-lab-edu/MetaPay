package com.ydmins.metapay.payment_service.controller;

import com.ydmins.metapay.payment_service.common.ApiResponse;
import com.ydmins.metapay.payment_service.common.ApiResult;
import com.ydmins.metapay.payment_service.domain.payment.dto.GetPaymentResponse;
import com.ydmins.metapay.payment_service.domain.payment.dto.PaymentRequest;
import com.ydmins.metapay.payment_service.exception.PaymentNotFoundException;
import com.ydmins.metapay.payment_service.exception.PaymentPersistenceException;
import com.ydmins.metapay.payment_service.exception.PaymentProcessingException;
import com.ydmins.metapay.payment_service.exception.PaymentServiceException;
import com.ydmins.metapay.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public boolean processPayment(@RequestBody PaymentRequest request){
        System.out.println("requested"+System.currentTimeMillis());
        try{
            return paymentService.processPayment(request);
        } catch (PaymentProcessingException e){
            // 추후 ResponseEntity 추가할 예정
            return false;
        }
    }

    @GetMapping("/{paymentId}")
    public ApiResponse<?> getPayment(@PathVariable  Long paymentId){
        try {
            GetPaymentResponse response =  paymentService.getPaymentDetail(paymentId);
            return ApiResponse.success(200, "Payment detail successfully retrieved", response);
        } catch (PaymentNotFoundException e){
            log.warn("Payment not found: {}", paymentId, e);
            return ApiResponse.notFound(404, "Payment not found", null);
        } catch (PaymentPersistenceException e){
            log.error("Payment persistence error: {}", paymentId, e);
            return ApiResponse.error(500, "Error retrieving payment", null);
        } catch (PaymentServiceException e){
            log.error("Payment service error: {}", paymentId, e);
            return ApiResponse.error(500, "Error retrieving payment", null);
        } catch (Exception e){
            log.error("An unexpected error occurred", e.getMessage());
            return ApiResponse.error(500, "An unexpected error occurred", null);
        }
    }
}
