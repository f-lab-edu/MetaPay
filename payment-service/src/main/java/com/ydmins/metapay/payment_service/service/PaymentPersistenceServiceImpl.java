package com.ydmins.metapay.payment_service.service;

import com.ydmins.metapay.payment_service.domain.payment.Payment;
import com.ydmins.metapay.payment_service.domain.payment.PaymentMethod;
import com.ydmins.metapay.payment_service.domain.payment.PaymentStatus;
import com.ydmins.metapay.payment_service.domain.payment.dto.PGResponse;
import com.ydmins.metapay.payment_service.domain.payment.dto.PaymentRequest;
import com.ydmins.metapay.payment_service.exception.PaymentNotFoundException;
import com.ydmins.metapay.payment_service.exception.PaymentPersistenceException;
import com.ydmins.metapay.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentPersistenceServiceImpl implements PaymentPersistenceService{
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean savePayment(Payment payment) throws PaymentPersistenceException {
        try{
            paymentRepository.save(payment);
            return true;
        } catch (DataAccessException e) {
            throw new PaymentPersistenceException("Failed to save payment result", e);
        }
    }

    @Override
    public Optional<Payment> findPayment(Long paymentId) {
        return paymentRepository.findById(paymentId);
    }
}
