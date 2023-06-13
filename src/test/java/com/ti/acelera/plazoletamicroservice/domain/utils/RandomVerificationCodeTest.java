package com.ti.acelera.plazoletamicroservice.domain.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomVerificationCodeTest {
    @Test
    void testGenerateSecurityPin() {
        String securityPin = RandomVerificationCode.generateSecurityPin();

        assertNotNull(securityPin);
        assertEquals(6, securityPin.length());
    }

}