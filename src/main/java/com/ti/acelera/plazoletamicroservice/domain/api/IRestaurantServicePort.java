package com.ti.acelera.plazoletamicroservice.domain.api;

import com.ti.acelera.plazoletamicroservice.domain.model.Restaurant;

public interface IRestaurantServicePort {
    void saveRestaurant(Restaurant restaurant);
    boolean verifyRestaurantOwner(String userId, Long restaurantId);
    void assignEmployee(String userId, Long restaurantId);
}
