package com.ti.acelera.plazoletamicroservice.adapters.http.controller;


import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.RestaurantRequestDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.UpdateDishRequestDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.handlers.IRestaurantHandler;
import com.ti.acelera.plazoletamicroservice.configuration.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class RestaurantRestController {

    private final IRestaurantHandler restaurantHandler;


    @PostMapping("add")
    public ResponseEntity<Map<String,String>> saveRestaurant(@Valid @RequestBody @Schema(
            description = "The request body",
            example = RestaurantRequestDto.example
    )RestaurantRequestDto restaurantRequestDto){
        restaurantHandler.saveRestaurant( restaurantRequestDto );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY, Constants.RESTAURANT_CREATED_MESSAGE));
    }

}
