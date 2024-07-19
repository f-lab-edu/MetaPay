package com.ydmins.metapay.payment_service.repository;

import com.ydmins.metapay.payment_service.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
