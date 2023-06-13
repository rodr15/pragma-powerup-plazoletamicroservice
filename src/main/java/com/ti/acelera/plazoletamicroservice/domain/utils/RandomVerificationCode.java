package com.ti.acelera.plazoletamicroservice.domain.utils;

import java.util.UUID;

public class RandomVerificationCode {
    private RandomVerificationCode() {
        throw new IllegalStateException("Utility class");
    }
    public static String generateSecurityPin() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

}
