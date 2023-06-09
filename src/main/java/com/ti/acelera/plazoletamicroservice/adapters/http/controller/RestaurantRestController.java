package com.ti.acelera.plazoletamicroservice.adapters.http.controller;


import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.OrderRequestDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.RestaurantRequestDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.dto.response.DishResponseDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.dto.response.OrderRestaurantResponseDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.dto.response.RestaurantResponseDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.handlers.IRestaurantHandler;
import com.ti.acelera.plazoletamicroservice.configuration.Constants;
import com.ti.acelera.plazoletamicroservice.domain.model.CategoryAveragePrice;
import com.ti.acelera.plazoletamicroservice.domain.model.OrderStatus;
import com.ti.acelera.plazoletamicroservice.domain.model.RestaurantStatistics;
import com.ti.acelera.plazoletamicroservice.domain.model.Traceability;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
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

    @SecurityRequirement(name = "jwt")
    @GetMapping("/order-list")
    public ResponseEntity<Page<OrderRestaurantResponseDto>> listOrder(@RequestAttribute("userId") String userId,
                                                                      @RequestParam OrderStatus state,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size) {


        return ResponseEntity.ok(restaurantHandler.getOrdersListByEmployeeId(parseLong(userId), state, page, size));
    }

    @SecurityRequirement(name = "jwt")
    @PutMapping("/assign-order")
    public ResponseEntity<Map<String, String>> assignOrder(@RequestAttribute("userId") String userId,
                                                           @RequestParam List<Long> ordersId) {
        restaurantHandler.assignEmployeeToOrders(userId, ordersId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY, Constants.EMPLOYEE_ASSIGN_TO_ORDER_MESSAGE));
    }

    @SecurityRequirement(name = "jwt")
    @PutMapping("/ready-order")
    public ResponseEntity<Map<String, String>> readyOrder(@RequestParam(defaultValue = "1") Long orderId) {
        restaurantHandler.finishRestaurantOrder(orderId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY, Constants.ORDER_READY_MESSAGE));
    }

    @SecurityRequirement(name = "jwt")
    @PutMapping("/deliver-order")
    public ResponseEntity<Map<String, String>> deliverOrder(
            @RequestAttribute("userId") String userId,
            @RequestParam(defaultValue = "1") Long orderId,
            @RequestParam String verificationCode) {

        restaurantHandler.deliverRestaurantOrder(orderId, verificationCode, parseLong(userId));

        return ResponseEntity.status(HttpStatus.OK)
                .body(Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY, Constants.ORDER_DELIVER_MESSAGE));
    }

    @SecurityRequirement(name = "jwt")
    @PutMapping("/cancel-order")
    public ResponseEntity<Map<String, String>> cancelOrder(
            @RequestAttribute("userId") String userId,
            @RequestParam(defaultValue = "1") Long orderId) {

        restaurantHandler.cancelOrder( parseLong(userId),orderId );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY, Constants.ORDER_CANCELED_MESSAGE));
    }

    @SecurityRequirement(name = "jwt")
    @GetMapping("/history-order")
    public ResponseEntity<List<Traceability>> historyOrder(
            @RequestAttribute("userId") String userId,
            @RequestParam(defaultValue = "1") Long orderId) {
        return ResponseEntity.ok(restaurantHandler.historyOrder( parseLong(userId),orderId ));
    }

    @SecurityRequirement(name = "jwt")
    @GetMapping("/statistics")
    public ResponseEntity<RestaurantStatistics> statistics(
            @RequestAttribute("userId") String userId,
            @RequestParam(defaultValue = "1") Long restaurantId
            ) {
        return ResponseEntity.ok(restaurantHandler.restaurantStatistics( parseLong(userId),restaurantId ));
    }

    @SecurityRequirement(name = "jwt")
    @GetMapping("/category-average-price")
    public ResponseEntity<List<CategoryAveragePrice>> averagePrice(@RequestAttribute("userId") String userId,
                                                                   @RequestParam(defaultValue = "1") Long restaurantId) {
        return ResponseEntity.ok(restaurantHandler.dishCategoryAveragePrice(parseLong(userId),restaurantId));

    }
    @SecurityRequirement(name = "jwt")
    @GetMapping("/delete")
    public ResponseEntity<Map<String,String>> delete(@RequestAttribute("userId") String userId,
                                                             @RequestParam(defaultValue = "1") Long restaurantId) {

        restaurantHandler.deleteRestaurant(parseLong(userId),restaurantId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY, Constants.ORDER_CANCELED_MESSAGE));

    }
}
