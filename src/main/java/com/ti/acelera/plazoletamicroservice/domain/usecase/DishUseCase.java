package com.ti.acelera.plazoletamicroservice.domain.usecase;

import com.ti.acelera.plazoletamicroservice.domain.api.IDishServicePort;
import com.ti.acelera.plazoletamicroservice.domain.exceptions.NotProprietaryGivenRestaurantException;
import com.ti.acelera.plazoletamicroservice.domain.exceptions.RestaurantNotExistsException;
import com.ti.acelera.plazoletamicroservice.domain.exceptions.RoleNotAllowedException;
import com.ti.acelera.plazoletamicroservice.domain.gateway.IUserClient;
import com.ti.acelera.plazoletamicroservice.domain.model.Dish;
import com.ti.acelera.plazoletamicroservice.domain.model.Restaurant;
import com.ti.acelera.plazoletamicroservice.domain.spi.IDishPersistencePort;
import com.ti.acelera.plazoletamicroservice.domain.spi.IRestaurantPersistencePort;

import java.util.Optional;

public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserClient userClient;

    public DishUseCase(IDishPersistencePort persistencePort, IRestaurantPersistencePort restaurantPersistencePort, IUserClient userClient) {
        this.dishPersistencePort = persistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userClient = userClient;
    }


    @Override
    public void saveDish(Dish dish) {

        Optional<Restaurant> restaurant = restaurantPersistencePort.getRestaurant(dish.getRestaurant().getId());

        if (restaurant.isEmpty()) {
            throw new RestaurantNotExistsException();
        }

        final String userId = "1231231231";// TODO: Replace when share token
        final String userRole = userClient.getRoleByDni(userId);

        if (!userRole.equals("ROLE_OWNER")) {
            throw new RoleNotAllowedException();
        }

        if (!restaurant.get().getIdProprietary().equals(userId)) {
            throw new NotProprietaryGivenRestaurantException();

        }

        dish.setActive(true);
        dishPersistencePort.saveDish(dish);
    }
}
