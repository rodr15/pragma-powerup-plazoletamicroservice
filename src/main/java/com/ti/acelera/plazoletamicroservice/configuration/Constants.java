package com.ti.acelera.plazoletamicroservice.configuration;

public class Constants {
    private Constants() {
        throw new IllegalStateException("Utility class");
    }
    public static final String RESPONSE_MESSAGE_KEY = "message";
    public static final String RESTAURANT_CREATED_MESSAGE = "Restaurant created successfully";
    public static final String RESPONSE_ERROR_MESSAGE_KEY = "error";
    public static final String PERSON_NOT_FOUND_MESSAGE = "No person found with the id provided";
    public static final String DISH_NOT_FOUND = "No dish found with the id provided";
    public static final String BAD_PAGED_MESSAGE = "The pagination is malformed";
    public static final String ROLE_NOT_ALLOWED_MESSAGE = "No permission granted to create restaurant for this role";
    public static final String RESTAURANT_NOT_EXISTS = "No restaurant found with the id provided";
    public static final String DISH_CREATED_MESSAGE = "Dish created successfully";
    public static final String DISH_UPDATED_MESSAGE = "Dish updated successfully";
    public static final String PERSON_NOT_PROPRIETARY_GIVEN_RESTAURANT = "The owner is not propietaary of the restaurant";
    public static final String SWAGGER_TITLE_MESSAGE = "Plazoleta API Pragma Power Up";
    public static final String SWAGGER_DESCRIPTION_MESSAGE = "Plazoleta microservice";
    public static final String SWAGGER_VERSION_MESSAGE = "1.0.0";
    public static final String SWAGGER_LICENSE_NAME_MESSAGE = "Apache 2.0";
    public static final String SWAGGER_LICENSE_URL_MESSAGE = "http://springdoc.org";
    public static final String SWAGGER_TERMS_OF_SERVICE_MESSAGE = "http://swagger.io/terms/";
}
