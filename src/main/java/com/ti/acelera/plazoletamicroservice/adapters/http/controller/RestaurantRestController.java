package com.ti.acelera.plazoletamicroservice.adapters.http.controller;


import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.OrderRequestDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.RestaurantRequestDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.dto.response.DishResponseDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.dto.response.RestaurantResponseDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.handlers.IRestaurantHandler;
import com.ti.acelera.plazoletamicroservice.configuration.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

import static java.lang.Long.parseLong;

@RestController
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class RestaurantRestController {

    private final IRestaurantHandler restaurantHandler;


    @SecurityRequirement(name = "jwt")
    @PostMapping("add")
    public ResponseEntity<Map<String, String>> saveRestaurant(@Valid @RequestBody @Schema(
            description = "The request body",
            example = RestaurantRequestDto.example
    ) RestaurantRequestDto restaurantRequestDto) {
        restaurantHandler.saveRestaurant(restaurantRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY, Constants.RESTAURANT_CREATED_MESSAGE));
    }

    @SecurityRequirement(name = "jwt")
    @PutMapping("add-employee")
    public ResponseEntity<Map<String, String>> assignEmployee(@RequestParam String userId, @RequestParam Long restaurantId) {
        restaurantHandler.assignRestaurantEmployee(userId, restaurantId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY, Constants.RESTAURANT_CREATED_MESSAGE));
    }

    //    @SecurityRequirement(name = "jwt")
    @GetMapping("/verify-owner")
    public ResponseEntity<Boolean> verifyOwner(@RequestParam String userId, @RequestParam Long restaurantId) {
        boolean isOwner = restaurantHandler.verifyRestaurantOwner(userId, restaurantId);
        return ResponseEntity.ok(isOwner);
    }

    @SecurityRequirement(name = "jwt")
    @GetMapping("/restaurant-list")
    public ResponseEntity<Page<RestaurantResponseDto>> pageRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<RestaurantResponseDto> pagedRestaurants = restaurantHandler.pageRestaurants(page, size);

        return ResponseEntity.ok(pagedRestaurants);
    }

    @SecurityRequirement(name = "jwt")
    @GetMapping("/{restaurantId}/menu")
    public ResponseEntity<Page<DishResponseDto>> pageRestaurantDishes(
            @PathVariable Long restaurantId,
            @RequestParam(defaultValue = "") Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(restaurantHandler.pageDishes(restaurantId, categoryId, page, size));
    }

    @SecurityRequirement(name = "jwt")
    @PostMapping("/order")
    public ResponseEntity<Map<String, String>> order(@RequestAttribute("userId") String userId,
                                                     @Valid @RequestBody @Schema(
                                                             description = "The request body",
                                                             example = OrderRequestDto.EXAMPLE
                                                     ) OrderRequestDto orderRequestDto) {
        restaurantHandler.makeOrder(parseLong(userId), orderRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY, Constants.ORDER_CREATED_MESSAGE));
    }
}
