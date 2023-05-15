package com.ti.acelera.plazoletamicroservice.adapters.http.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateDishRequestDto {
    private Long price;
    private String description;
}
