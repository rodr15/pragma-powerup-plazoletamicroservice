package com.ti.acelera.plazoletamicroservice.adapters.http.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
@AllArgsConstructor
@Getter
public class OrderRequestDto {
    @Positive
    private Long restaurantId;
    @NotNull(message = "Dishes must have one or more order dish")
    @Size(min=1,message = "Dishes must have one or more order dish")
    private List<DishOrderRequestDto> dishes;


    public static final String EXAMPLE = "{" +
            "\"restaurantId\": 1," +
            "\"dishes\": [" +
            DishOrderRequestDto.EXAMPLE +
            "]" +
            "}";


}
