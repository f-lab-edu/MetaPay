package com.ydmins.metapay.payment_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HealthServiceImpl implements HealthService, HealthIndicator {

    private final DataSource dataSource;

    @Override
    public Health checkHealth() {
        Map<String, Object> details = new HashMap<>();
        details.put("service", checkService());
        details.put("database", checkDatabase());

        boolean isHealthy = (boolean) details.get("service")
                && (boolean) details.get("database");

        return isHealthy ?
                Health.up().withDetails(details).build() :
                Health.down().withDetails(details).build();
    }

    private boolean checkService(){
        return true;
    }

    private boolean checkDatabase(){
        try{
            Connection conn = dataSource.getConnection();
            return conn.isValid(3000);
        } catch (SQLException e){
            return false;
        }
    }


    @Override
    public Health health() {
        return checkHealth();
    }
}
