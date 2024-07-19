package com.ydmins.metapay.payment_service.domain.payment.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class PGResponse {
    private boolean success;
    private String transactionId;
    private BigDecimal amount;
    private String message;
}
