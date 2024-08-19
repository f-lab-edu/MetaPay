package com.ydmins.metapay.payment_service.handler;

import com.ydmins.metapay.payment_service.domain.payment.Payment;
import com.ydmins.metapay.payment_service.domain.payment.PaymentStatus;
import com.ydmins.metapay.payment_service.domain.payment.dto.PGResponse;
import com.ydmins.metapay.payment_service.repository.PaymentRepository;
import com.ydmins.metapay.payment_service.service.PGService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentInconsistencyHandler {
    private final PaymentRepository paymentRepository;
    private final PGService pgService;

    @Async
    public void handleInconsistency(Payment payment) {
        try {
            PGResponse latestResponse = pgService.checkPaymentStatus(payment);
            if (!latestResponse.isSuccess()) {
                pgService.cancelPayment(payment.getIdempotencyKey());
                log.info("Cancelled inconsistent payment with idempotency key: {}", payment.getIdempotencyKey());
            } else {
                log.error("CRITICAL : Payment is successful in PG but failed to save locally. Manual reconciliation " +
                        "needed for idempotency key: {}", payment.getIdempotencyKey());
            }
        } catch (Exception e) {
            log.error("Failed to handle payment inconsistency for idempotency key: {}", payment.getIdempotencyKey(), e);
        }
    }

}
