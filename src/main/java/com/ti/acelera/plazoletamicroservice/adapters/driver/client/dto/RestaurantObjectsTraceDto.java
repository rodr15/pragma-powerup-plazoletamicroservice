package com.ti.acelera.plazoletamicroservice.adapters.driver.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RestaurantObjectsTraceDto {
    private Long restaurantId;
    private String state;
    private String objectId;
    private String objectName;
    private String objectType;
}
