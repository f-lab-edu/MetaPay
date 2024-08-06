package com.ydmins.metapay.payment_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class HealthServiceImpl implements HealthService, HealthIndicator {

    private final DataSource dataSource;

    private static final String SERVICE = "service";
    private static final String DATABASE = "database";

    @Override
    public Health checkHealth() {
        boolean isServiceHealthy = checkService();
        boolean isDatabaseHealthy = checkDatabase();

        Health.Builder healthBuilder = Health.status("")
                .withDetail(SERVICE, isServiceHealthy)
                .withDetail(DATABASE, isDatabaseHealthy);

        boolean isHealthy = isServiceHealthy && isDatabaseHealthy;

        return isHealthy
                ? healthBuilder.up().build()
                : healthBuilder.down().build();
    }

    private boolean checkService(){
        return true;
    }

    private boolean checkDatabase(){
        try{
            Connection conn = dataSource.getConnection();
            return conn.isValid(3000);
        } catch (SQLException e){
            log.error("Database connection validation failed", e);
            return false;
        }
    }


    @Override
    public Health health() {
        return checkHealth();
    }
}
