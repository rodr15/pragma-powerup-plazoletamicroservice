package com.ti.acelera.plazoletamicroservice.domain.usecase;

import com.ti.acelera.plazoletamicroservice.domain.api.IDishServicePort;
import com.ti.acelera.plazoletamicroservice.domain.exceptions.RestaurantNotExists;
import com.ti.acelera.plazoletamicroservice.domain.model.Dish;
import com.ti.acelera.plazoletamicroservice.domain.spi.IDishPersistencePort;
import com.ti.acelera.plazoletamicroservice.domain.spi.IRestaurantPersistencePort;

public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;

    public DishUseCase(IDishPersistencePort persistencePort, IRestaurantPersistencePort restaurantPersistencePort) {
        this.dishPersistencePort = persistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
    }


    @Override
    public void saveDish(Dish dish) {

        if(!restaurantPersistencePort.restaurantExists( dish.getIdRestaurant() )){
            throw new RestaurantNotExists();
        }

        dish.setActive( true );
        dishPersistencePort.saveDish( dish );
    }
}
