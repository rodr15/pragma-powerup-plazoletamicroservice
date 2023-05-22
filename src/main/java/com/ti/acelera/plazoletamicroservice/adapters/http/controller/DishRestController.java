package com.ti.acelera.plazoletamicroservice.adapters.http.controller;

import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.DishRequestDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.RestaurantRequestDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.UpdateDishRequestDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.handlers.IDishHandler;
import com.ti.acelera.plazoletamicroservice.configuration.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/dish")
@RequiredArgsConstructor
public class DishRestController {

    private final IDishHandler dishHandler;

    @SecurityRequirement(name = "jwt")
    @PostMapping("add")
    public ResponseEntity<Map<String,String>> saveRestaurant(@Valid @RequestBody @Schema(
            description = "The request body",
            example = DishRequestDto.example
    )DishRequestDto dishRequestDto){

        dishHandler.saveDish( dishRequestDto );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY, Constants.DISH_CREATED_MESSAGE));
    }


    @SecurityRequirement(name = "jwt")
    @PostMapping("update/{dishId}")
    public ResponseEntity<Map<String,String>> updateDish(@PathVariable Long dishId, @Valid @RequestBody @Schema(
            description = "The request body",
            example = UpdateDishRequestDto.example
    ) UpdateDishRequestDto updateDishRequestDto){
        dishHandler.modifyDish(dishId, updateDishRequestDto );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY, Constants.DISH_CREATED_MESSAGE));
    }


}
