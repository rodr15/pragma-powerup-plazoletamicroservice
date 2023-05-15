package com.ti.acelera.plazoletamicroservice.adapters.http.handlers;


import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.RestaurantRequestDto;
import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.UpdateDishRequestDto;

public interface IRestaurantHandler {
    void saveRestaurant(RestaurantRequestDto restaurantRequestDto);

}
