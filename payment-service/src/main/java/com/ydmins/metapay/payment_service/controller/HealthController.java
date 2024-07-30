package com.ydmins.metapay.payment_service.controller;

import com.ydmins.metapay.payment_service.service.HealthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment/health")
public class HealthController {

    private final HealthService healthService;

    @GetMapping
    public Health check(){
        return healthService.checkHealth();
    }

}
