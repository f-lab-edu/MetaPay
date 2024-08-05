package com.ydmins.metapay.payment_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class HealthServiceImplTest {

    @Mock
    private DataSource dateSource;

    @Mock
    private Connection connection;

    private HealthServiceImpl healthService;

    @BeforeEach
    void setUP() throws SQLException{
        MockitoAnnotations.openMocks(this);
        healthService = new HealthServiceImpl(dateSource);
        when(dateSource.getConnection()).thenReturn(connection);
    }

    private void assertHealthDetailsMatch(Health health, Status status, boolean serviceHealth, boolean databaseHealth){
        assertEquals(status, health.getStatus());
        assertEquals(serviceHealth, health.getDetails().get("service"));
        assertEquals(databaseHealth, health.getDetails().get("database"));
    }

    @Test
    void checkHealthAllHealthy() throws SQLException{
        // when
        when(connection.isValid(3000)).thenReturn(true);
        Health health = healthService.checkHealth();

        // then
        assertHealthDetailsMatch(health, Status.UP, true, true);
    }

    @Test
    void checkHealthDatabaseUnhealthy() throws SQLException{
        // when
        when(connection.isValid(3000)).thenReturn(false);
        Health health = healthService.checkHealth();

        // then
        assertHealthDetailsMatch(health, Status.DOWN, true, false);
    }

    @Test
    void checkHealthDatabaseException() throws SQLException{
        // when
        when(connection.isValid(3000)).thenThrow(new SQLException("Database error"));
        Health health = healthService.checkHealth();

        // then
        assertHealthDetailsMatch(health, Status.DOWN, true, false);
    }


    @Test
    void healthReturnsSameAsCheckHealth() throws SQLException{
        // when
        when(connection.isValid(3000)).thenReturn(true);
        Health healthFrom_checkHealth = healthService.checkHealth();
        Health healthFrom_Health = healthService.health();

        // then
        assertEquals(healthFrom_checkHealth.getStatus(), healthFrom_Health.getStatus());
        assertEquals(healthFrom_checkHealth.getDetails(), healthFrom_Health.getDetails());
    }
}