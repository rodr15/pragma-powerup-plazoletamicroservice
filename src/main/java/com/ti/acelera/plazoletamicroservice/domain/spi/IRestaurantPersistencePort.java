package com.ti.acelera.plazoletamicroservice.domain.spi;

import com.ti.acelera.plazoletamicroservice.domain.model.Restaurant;
import com.ti.acelera.plazoletamicroservice.domain.model.RestaurantState;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IRestaurantPersistencePort {
    void saveRestaurant(Restaurant restaurant);

    Optional<Long> getRestaurantIdByEmployeeId(Long employeeId);
    boolean restaurantExists(Long id);
    Optional<Restaurant> getRestaurant(Long id);
    Page<Restaurant> getAllRestaurants(int page ,int size);
    List<Restaurant> getAllRestaurantsByState(RestaurantState state);
    void deleteRestaurant(Restaurant restaurant);

}
