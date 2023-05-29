package com.ti.acelera.plazoletamicroservice.domain.spi;

import com.ti.acelera.plazoletamicroservice.domain.model.Restaurant;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface IRestaurantPersistencePort {
    void saveRestaurant(Restaurant restaurant);

    boolean restaurantExists(Long id);
    Optional<Restaurant> getRestaurant(Long id);
    Page<Restaurant> getAllRestaurants(int page ,int size);

}
