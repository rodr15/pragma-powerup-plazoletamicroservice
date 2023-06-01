package com.ti.acelera.plazoletamicroservice.adapters.http.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DishOrderRequestDto {
    @NotBlank(message = "Mandatory")
    private Long dishId;
    @Positive
    private  Long quantity;

    public static final String EXAMPLE =  "{" +
            "\"dishId\": 1," +
            "\"quantity\": 2" +
            "}";
}
