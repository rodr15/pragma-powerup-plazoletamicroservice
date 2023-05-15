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

        Optional<Restaurant> restaurant = restaurantPersistencePort.getRestaurant(dish.getIdRestaurant());

        if (restaurant.isEmpty()) {
            throw new RestaurantNotExistsException();
        }

        final String userId = "1231231231";// TODO: Replace when share token

        verifyOwner(userId, restaurant.get().getIdProprietary());

        dish.setActive(true);
        dishPersistencePort.saveDish(dish);
    }

    @Override
    public void modifyDish(Long id, Long price, String description) {
        Optional<Dish> dish = dishPersistencePort.getDish(id);

        if (dish.isEmpty()) {
            throw new RuntimeException(); //TODO: Change exception
        }

        if (price != null) {
            dish.get().setPrice(price);
        }
        if (description != null) {
            dish.get().setDescription(description);
        }

        dishPersistencePort.saveDish(dish.get());
    }

    private void verifyOwner(String userId, String restaurantId) {
        final String userRole = userClient.getRoleByDni(userId);

        if (!userRole.equals("ROLE_OWNER")) {
            throw new RoleNotAllowedException();
        }

        if (!restaurantId.equals(userId)) {
            throw new NotProprietaryGivenRestaurantException();

        }
    }

}
