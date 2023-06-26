package com.ti.acelera.plazoletamicroservice.adapters.http.controller;

import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.DishRequestDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.UpdateDishRequestDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.dto.response.DishResponseDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.handlers.IDishHandler;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dish")
@RequiredArgsConstructor
public class DishRestController {

    private final IDishHandler dishHandler;


    @SecurityRequirement(name = "jwt")
    @PostMapping("add")
    public ResponseEntity<Map<String, String>> saveRestaurant(@Valid @RequestBody @Schema(
            description = "The request body",
            example = DishRequestDto.example
    ) DishRequestDto dishRequestDto, @RequestAttribute("userId") String userId
    ) {
        dishHandler.saveDish(userId, dishRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY, Constants.DISH_CREATED_MESSAGE));
    }


    @SecurityRequirement(name = "jwt")
    @PutMapping("update/{dishId}")
    public ResponseEntity<Map<String, String>> updateDish(@PathVariable Long dishId, @Valid @RequestBody @Schema(
            description = "The request body",
            example = UpdateDishRequestDto.example
    ) UpdateDishRequestDto updateDishRequestDto, @RequestAttribute("userId") String userId) {
        dishHandler.modifyDish(userId, dishId, updateDishRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY, Constants.DISH_UPDATED_MESSAGE));
    }

    @SecurityRequirement(name = "jwt")
    @PutMapping("enable-disable/{dishId}/{dishState}")
    public ResponseEntity<Map<String, String>> enableDish(@RequestAttribute("userId") String userId, @PathVariable Long dishId, @PathVariable boolean dishState) {
        dishHandler.modifyDishState(userId, dishId, dishState);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY, Constants.DISH_UPDATED_MESSAGE));
    }


    @SecurityRequirement(name = "jwt")
    @GetMapping("/dish-search-budget")
    public ResponseEntity<Page<DishResponseDto>> searchDishByBudget(
            @RequestParam(defaultValue = "0") Long lowBudget,
            @RequestParam(defaultValue = "100") Long upBudget,
            @RequestParam(required = false) List<Long> preferenceCategories,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {

        return ResponseEntity.ok(dishHandler.getDishesByBudgetAndCategoryPreferences(lowBudget,upBudget, preferenceCategories, page, size));

    }

}
