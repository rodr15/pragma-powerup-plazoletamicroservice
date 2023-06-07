package com.ti.acelera.plazoletamicroservice.adapters.http.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DishOrderResponseDto {
    private Long dishId;
    private String name;
    private  Long quantity;
}
