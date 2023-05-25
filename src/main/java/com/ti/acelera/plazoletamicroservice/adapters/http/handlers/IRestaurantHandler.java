package com.ti.acelera.plazoletamicroservice.adapters.http.handlers;


import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.RestaurantRequestDto;

public interface IRestaurantHandler {
    void saveRestaurant(RestaurantRequestDto restaurantRequestDto);
    void assignRestaurantEmployee(String employeeId , Long restaurantId);
    boolean verifyRestaurantOwner( String userid,  Long restaurantId );

}
