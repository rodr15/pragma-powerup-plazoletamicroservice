package com.ti.acelera.plazoletamicroservice.domain.api;

import com.ti.acelera.plazoletamicroservice.domain.model.Restaurant;

import java.util.List;

public interface IRestaurantServicePort {
    void saveRestaurant(Restaurant restaurant);
    boolean verifyRestaurantOwner(String userId, Long restaurantId);
}
