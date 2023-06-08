package com.ti.acelera.plazoletamicroservice.configuration;

public class Constants {
    private Constants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String EARRING_ORDER = "EARRING";
    public static final String IN_PREPARATION_ORDER = "IN PREPARATION";
    public static final String READY_ORDER = "READY";
    public static final String FINISHED_ORDER = "FINISHED";
    public static final String RESPONSE_MESSAGE_KEY = "message";
    public static final String RESTAURANT_CREATED_MESSAGE = "Restaurant created successfully";
    public static final String ORDER_CREATED_MESSAGE = "Order created successfully";
    public static final String RESPONSE_ERROR_MESSAGE_KEY = "error";
    public static final String HAS_UNFINISHED_ORDERS = "This user has unfinished orders";
    public static final String BAD_ORDER_MESSAGE = "The order is malformed";
    public static final String ORDER_NOT_FOUND_MESSAGE = "No order found with the one or more of the ids provided";
    public static final String EMPLOYEE_ASSIGN_TO_ORDER_MESSAGE = "The employee has been assign to the orders successfully";
    public static final String PERSON_NOT_FOUND_MESSAGE = "No person found with the id provided";
    public static final String EMPLOYEE_NOT_FOUND_MESSAGE = "No employee found with the id provided";
    public static final String DISH_NOT_FOUND = "No dish found with the id provided";
    public static final String BAD_PAGED_MESSAGE = "The pagination is malformed";
    public static final String ROLE_NOT_ALLOWED_MESSAGE = "No permission granted to create restaurant for this role";
    public static final String RESTAURANT_NOT_EXISTS = "No restaurant found with the id provided";
    public static final String DISH_CREATED_MESSAGE = "Dish created successfully";
    public static final String DISH_UPDATED_MESSAGE = "Dish updated successfully";
    public static final String PERSON_NOT_PROPRIETARY_GIVEN_RESTAURANT = "The owner is not proprietary of the restaurant";
    public static final String SWAGGER_TITLE_MESSAGE = "Plazoleta API Pragma Power Up";
    public static final String SWAGGER_DESCRIPTION_MESSAGE = "Plazoleta microservice";
    public static final String SWAGGER_VERSION_MESSAGE = "1.0.0";
    public static final String SWAGGER_LICENSE_NAME_MESSAGE = "Apache 2.0";
    public static final String SWAGGER_LICENSE_URL_MESSAGE = "http://springdoc.org";
    public static final String SWAGGER_TERMS_OF_SERVICE_MESSAGE = "http://swagger.io/terms/";
}
