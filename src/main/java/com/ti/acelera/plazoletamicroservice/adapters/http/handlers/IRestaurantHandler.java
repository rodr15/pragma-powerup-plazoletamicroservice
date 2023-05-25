package com.ti.acelera.plazoletamicroservice.adapters.http.handlers;


import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.RestaurantRequestDto;

import java.util.List;

public interface IRestaurantHandler {
    void saveRestaurant(RestaurantRequestDto restaurantRequestDto);
    boolean verifyRestaurantOwner( String userid,  Long restaurantId );

}
