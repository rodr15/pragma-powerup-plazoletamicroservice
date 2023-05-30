package com.ti.acelera.plazoletamicroservice.adapters.http.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.Map;

public class OrderRequestDto {
    @NotBlank(message = "Mandatory")
    private String idRestaurant;
    @NotBlank(message = "Should order one or more dishes")
    private Map<Long,Integer> order;

}
