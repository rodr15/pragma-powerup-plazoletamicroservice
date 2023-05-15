package com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.adapter;


import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.entity.RestaurantEntity;
import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.mappers.IRestaurantEntityMapper;
import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.repositories.IRestaurantRepository;
import com.ti.acelera.plazoletamicroservice.domain.model.Restaurant;
import com.ti.acelera.plazoletamicroservice.domain.spi.IRestaurantPersistencePort;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class RestaurantMysqlAdapter implements IRestaurantPersistencePort {
    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;

    @Override
    public void saveRestaurant(Restaurant restaurant) {
        restaurantRepository.save(restaurantEntityMapper.toEntity(restaurant));
    }

    @Override
    public boolean restaurantExists(Long id) {
        return restaurantRepository.existsById(id);
    }

    @Override
    public Optional<Restaurant> getRestaurant(Long id) {

        Optional<RestaurantEntity> restaurant = restaurantRepository.findById(id);

        if (restaurant.isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(restaurantEntityMapper.toRestaurant(restaurant.get()));

    }
}
