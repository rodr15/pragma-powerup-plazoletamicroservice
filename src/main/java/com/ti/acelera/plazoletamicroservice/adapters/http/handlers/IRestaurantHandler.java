package com.ti.acelera.plazoletamicroservice.adapters.http.handlers;


import com.ti.acelera.plazoletamicroservice.adapters.http.dto.request.RestaurantRequestDto;
import org.springframework.data.domain.Page;

public interface IRestaurantHandler {
    Page<RestaurantRequestDto> pageRestaurants(int page, int size);
    void saveRestaurant(RestaurantRequestDto restaurantRequestDto);
    void assignRestaurantEmployee(String employeeId , Long restaurantId);
    boolean verifyRestaurantOwner( String userid,  Long restaurantId );

}
