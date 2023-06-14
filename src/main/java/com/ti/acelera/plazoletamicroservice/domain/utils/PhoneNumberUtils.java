package com.ti.acelera.plazoletamicroservice.domain.utils;

public class PhoneNumberUtils {
    private PhoneNumberUtils() {
        throw new IllegalStateException("Utility class");
    }
    public static String isValidColombianCellphoneNumber(String phoneNumber) {
        String digitsOnly = phoneNumber.replaceAll("\\D", "");
        if (digitsOnly.startsWith("3")) {
            digitsOnly = "+57" + digitsOnly;
        }

        if (!digitsOnly.startsWith("+")) {
            digitsOnly = "+" + digitsOnly;
        }
        if (digitsOnly.length() != 13) {
            throw new IllegalArgumentException("Invalid phone number format");
        }

        return digitsOnly;
    }
}
