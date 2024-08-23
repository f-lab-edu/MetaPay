package com.ydmins.metapay.payment_service.service;

import com.ydmins.metapay.payment_service.domain.payment.Payment;
import com.ydmins.metapay.payment_service.exception.PaymentPersistenceException;

public interface PaymentPersistenceService {

    boolean savePayment(Payment payment) throws PaymentPersistenceException;
}
