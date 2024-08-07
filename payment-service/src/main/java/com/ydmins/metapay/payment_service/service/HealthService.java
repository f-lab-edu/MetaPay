package com.ydmins.metapay.payment_service.service;

import org.springframework.boot.actuate.health.Health;

public interface HealthService {

    Health checkHealth();
}
