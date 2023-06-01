package com.ti.acelera.plazoletamicroservice.adapters.http.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
@AllArgsConstructor
@Getter
public class OrderRequestDto {
    @Positive
    private Long restaurantId;
    private List<DishOrderRequestDto> dishes;


    public static final String EXAMPLE = "{" +
            "\"restaurantId\": 1," +
            "\"dishes\": [" +
            DishOrderRequestDto.EXAMPLE +
            "]" +
            "}";


}
