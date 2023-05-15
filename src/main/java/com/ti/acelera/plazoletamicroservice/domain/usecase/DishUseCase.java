package com.ti.acelera.plazoletamicroservice.domain.usecase;

import com.ti.acelera.plazoletamicroservice.domain.api.IDishServicePort;
import com.ti.acelera.plazoletamicroservice.domain.exceptions.RestaurantNotExistsException;
import com.ti.acelera.plazoletamicroservice.domain.exceptions.RoleNotAllowedException;
import com.ti.acelera.plazoletamicroservice.domain.gateway.IUserClient;
import com.ti.acelera.plazoletamicroservice.domain.model.Dish;
import com.ti.acelera.plazoletamicroservice.domain.spi.IDishPersistencePort;
import com.ti.acelera.plazoletamicroservice.domain.spi.IRestaurantPersistencePort;

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

        if(!restaurantPersistencePort.restaurantExists( dish.getIdRestaurant() )){
            throw new RestaurantNotExistsException();
        }

        final String userId = "2";// TODO: Replace when share token
        final String userRole = userClient.getRoleByDni(userId);

        if (!userRole.equals("ROLE_OWNER")){
            throw new RoleNotAllowedException();
        }

        dish.setActive( true );
        dishPersistencePort.saveDish( dish );
    }
}
