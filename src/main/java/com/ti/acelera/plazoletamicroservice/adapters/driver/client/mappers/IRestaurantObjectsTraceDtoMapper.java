package com.ti.acelera.plazoletamicroservice.adapters.driver.client.mappers;

import com.ti.acelera.plazoletamicroservice.adapters.driver.client.dto.RestaurantObjectsTraceDto;
import com.ti.acelera.plazoletamicroservice.domain.model.RestaurantObjectsTrace;

public interface IRestaurantObjectsTraceDtoMapper {
    RestaurantObjectsTraceDto toRestaurantObjectsTraceDto(RestaurantObjectsTrace restaurantObjectsTrace);
}
