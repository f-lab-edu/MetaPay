package com.ydmins.metapay.payment_service.domain.payment.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public class PGResponse {
    private boolean success;
    private String transactionId;
    private BigDecimal amount;
    private String message;
}
