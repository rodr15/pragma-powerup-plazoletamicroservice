package com.ti.acelera.plazoletamicroservice.configuration;

import com.ti.acelera.plazoletamicroservice.adapters.driver.client.exceptions.UserNotFoundException;
import com.ti.acelera.plazoletamicroservice.domain.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.Map;

import static com.ti.acelera.plazoletamicroservice.configuration.Constants.*;

@ControllerAdvice
public class ControllerAdvisor {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleAuthenticationException(UserNotFoundException noDataFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, PERSON_NOT_FOUND_MESSAGE));
    }
    @ExceptionHandler(RoleNotAllowedException.class)
    public ResponseEntity<Map<String, String>> handleRoleNotAllowedForCreationException(
            RoleNotAllowedException roleNotAllowedForCreationException) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, ROLE_NOT_ALLOWED_MESSAGE));
    }
    @ExceptionHandler(RestaurantNotExistsException.class)
    public ResponseEntity<Map<String, String>> handleRestaurantNotExistsException(
            RestaurantNotExistsException restaurantNotExistsException) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, RESTAURANT_NOT_EXISTS));
    }
    @ExceptionHandler(NotProprietaryGivenRestaurantException.class)
    public ResponseEntity<Map<String, String>> handleNotProprietaryGivenRestaurantException(
            NotProprietaryGivenRestaurantException notProprietaryGivenRestaurantException) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, PERSON_NOT_PROPRIETARY_GIVEN_RESTAURANT));
    }
    @ExceptionHandler(DishNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleDishNotFoundException(
            DishNotFoundException dishNotFoundException) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, DISH_NOT_FOUND));
    }
    @ExceptionHandler(BadPagedException.class)
    public ResponseEntity<Map<String, String>> handleBadPagedException(
            BadPagedException badPagedException) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, BAD_PAGED_MESSAGE));
    }
    @ExceptionHandler(ThisClientHasUnfinishedOrdersException.class)
    public ResponseEntity<Map<String, String>> handleThisClientHasUnfinishedOrders(
            ThisClientHasUnfinishedOrdersException thisClientHasUnfinishedOrders) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, HAS_UNFINISHED_ORDERS));
    }
    @ExceptionHandler(MalformedOrderException.class)
    public ResponseEntity<Map<String, String>> handleMalformedOrderException(
            MalformedOrderException thisMalformedOrderException) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, BAD_ORDER_MESSAGE));
    }
    @ExceptionHandler(EmployeeNotFindException.class)
    public ResponseEntity<Map<String, String>> handleEmployeeNotAssignToAnyRestaurantException(
            EmployeeNotFindException thisEmployeeNotAssignToAnyRestaurantException) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY,EMPLOYEE_NOT_FOUND_MESSAGE ));
    }

    @ExceptionHandler(OrdersNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleOrdersNotFoundException(
            OrdersNotFoundException thisOrdersNotFoundException) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, ORDER_NOT_FOUND_MESSAGE));
    }
    @ExceptionHandler(OrderNotAssignedException.class)
    public ResponseEntity<Map<String, String>> handleOrderNotAssignedException(
            OrderNotAssignedException thisOrderNotAssignedException) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, ORDER_NOT_ASSIGN_MESSAGE));
    }

    @ExceptionHandler(NotAReadyOrderException.class)
    public ResponseEntity<Map<String, String>> handleNotAReadyOrderException(
            NotAReadyOrderException thisNotAReadyOrderException) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, NOT_ORDER_STATUS_REQUIRED_MESSAGE));
    }

    @ExceptionHandler(WrongVerificationCodeException.class)
    public ResponseEntity<Map<String, String>> handleWrongVerificationCodeException(
            WrongVerificationCodeException thisWrongVerificationCodeException) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, WRONG_VERIFICATION_CODE_MESSAGE));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleOrderNotFoundException(
            OrderNotFoundException thisOrderNotFoundException) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, ORDER_NOT_FOUND_MESSAGE));
    }

    @ExceptionHandler(OrderStatusNotAllowedForThisActionException.class)
    public ResponseEntity<Map<String, String>> handleOrderStatusNotAllowedForThisActionException(
            OrderStatusNotAllowedForThisActionException thisOrderStatusNotAllowedForThisActionException) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, INCORRECT_ORDER_STATUS));
    }

    @ExceptionHandler(RestaurantDontHaveRegisteredEmployeesException.class)
    public ResponseEntity<Map<String, String>> handleRestaurantDontHaveRegisteredEmployeesException(
            RestaurantDontHaveRegisteredEmployeesException thisRestaurantDontHaveRegisteredEmployeesException) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap(RESPONSE_ERROR_MESSAGE_KEY, RESTAURANT_WITHOUT_EMPLOYEES_MESSAGE));
    }

}
