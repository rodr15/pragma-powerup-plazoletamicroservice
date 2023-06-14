package com.ti.acelera.plazoletamicroservice.domain.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhoneNumberUtilsTest {

    @Test
    void testCorrectPhoneNumber_ValidPhoneNumber() {
        // Arrange
        String phoneNumber = "3123456789";
        String expected = "+573123456789";

        // Act
        String result = PhoneNumberUtils.isValidColombianCellphoneNumber(phoneNumber);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    void testCorrectPhoneNumber_ValidPhoneNumberWithPlus() {
        // Arrange
        String phoneNumber = "+573123456789";
        String expected = "+573123456789";

        // Act
        String result = PhoneNumberUtils.isValidColombianCellphoneNumber(phoneNumber);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    void testCorrectPhoneNumber_InvalidPhoneNumber() {
        // Arrange
        String phoneNumber = "123456789";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> PhoneNumberUtils.isValidColombianCellphoneNumber(phoneNumber));
    }

    @Test
    void testCorrectPhoneNumber_InvalidPhoneNumberLength() {
        // Arrange
        String phoneNumber = "+57312345678";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> PhoneNumberUtils.isValidColombianCellphoneNumber(phoneNumber));
    }

}