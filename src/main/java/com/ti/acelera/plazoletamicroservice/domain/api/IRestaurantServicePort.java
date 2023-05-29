package com.ti.acelera.plazoletamicroservice.domain.api;

import com.ti.acelera.plazoletamicroservice.domain.model.Restaurant;
import org.springframework.data.domain.Page;

public interface IRestaurantServicePort {
    Page<Restaurant> pageRestaurants(int page, int size);
    void saveRestaurant(Restaurant restaurant);
    boolean verifyRestaurantOwner(String userId, Long restaurantId);
    void assignEmployee(String userId, Long restaurantId);
}
