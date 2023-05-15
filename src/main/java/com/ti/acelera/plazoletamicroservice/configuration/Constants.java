package com.ti.acelera.plazoletamicroservice.configuration;

public class Constants {
    private Constants() {
        throw new IllegalStateException("Utility class");
    }
    public static final String RESPONSE_MESSAGE_KEY = "message";
    public static final String RESTAURANT_CREATED_MESSAGE = "Restaurant created successfully";
    public static final String RESPONSE_ERROR_MESSAGE_KEY = "error";
    public static final String PERSON_NOT_FOUND_MESSAGE = "No person found with the id provided";
    public static final String ROLE_NOT_ALLOWED_MESSAGE = "No permission granted to create restaurant for this role";
    public static final String RESTAURANT_NOT_EXISTS = "No restaurant found with the id provided";
}
