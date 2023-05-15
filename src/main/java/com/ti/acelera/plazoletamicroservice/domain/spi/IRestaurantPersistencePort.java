package com.ti.acelera.plazoletamicroservice.domain.spi;

import com.ti.acelera.plazoletamicroservice.domain.model.Restaurant;

public interface IRestaurantPersistencePort {
    void saveRestaurant(Restaurant restaurant);

    boolean restaurantExists(Long id);

}
