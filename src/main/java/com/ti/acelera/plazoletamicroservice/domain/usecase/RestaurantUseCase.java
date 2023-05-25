package com.ti.acelera.plazoletamicroservice.domain.usecase;

import com.ti.acelera.plazoletamicroservice.domain.api.IRestaurantServicePort;
import com.ti.acelera.plazoletamicroservice.domain.exceptions.RoleNotAllowedException;
import com.ti.acelera.plazoletamicroservice.domain.gateway.IUserClient;
import com.ti.acelera.plazoletamicroservice.domain.model.Restaurant;
import com.ti.acelera.plazoletamicroservice.domain.spi.IRestaurantPersistencePort;

import java.util.List;
import java.util.Optional;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserClient userClient;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort, IUserClient userClient) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userClient = userClient;
    }

    @Override
    public void saveRestaurant(Restaurant restaurant) {
        final String role = userClient.getRoleByDni(restaurant.getIdProprietary());

        if (!role.equals("ROLE_OWNER")){
            throw new RoleNotAllowedException();
        }

        restaurantPersistencePort.saveRestaurant( restaurant );
    }

    @Override
    public boolean verifyRestaurantOwner(String userId, Long restaurantId) {

        Optional<Restaurant> restaurant =  restaurantPersistencePort.getRestaurant( restaurantId );

        if(restaurant.isEmpty()) return false;

        return restaurant.get().getIdProprietary().equals(userId);
    }
}
