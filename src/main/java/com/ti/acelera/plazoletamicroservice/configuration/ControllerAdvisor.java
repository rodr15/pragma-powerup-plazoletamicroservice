package com.ti.acelera.plazoletamicroservice.configuration;

import com.ti.acelera.plazoletamicroservice.adapters.driver.client.exceptions.UserNotFoundException;
import com.ti.acelera.plazoletamicroservice.domain.exceptions.NotProprietaryGivenRestaurantException;
import com.ti.acelera.plazoletamicroservice.domain.exceptions.RestaurantNotExistsException;
import com.ti.acelera.plazoletamicroservice.domain.exceptions.RoleNotAllowedException;
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
}
