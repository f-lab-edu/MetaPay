package com.ydmins.metapay.payment_service.domain.payment.dto;

import com.ydmins.metapay.payment_service.domain.payment.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class PaymentRequest {
    @NotNull(message = "금액을 입력해주세요.")
    @Positive
    private BigDecimal amount;
    @NotNull(message = "결제 수단을 선택해주세요.")
    private PaymentMethod method;
    @NotNull(message = "사용자 아이디를 입력해주세요.")
    private String userId;
    @NotNull(message = "주문번호를 입력해주세요.")
    private String orderId;
    @NotNull(message = "멱등성 키를 입력해주세요.")
    private String idempotencyKey;
}
