package com.ti.acelera.plazoletamicroservice.domain.usecase;

import com.ti.acelera.plazoletamicroservice.domain.api.IDishServicePort;
import com.ti.acelera.plazoletamicroservice.domain.exceptions.DishNotFoundException;
import com.ti.acelera.plazoletamicroservice.domain.exceptions.NotProprietaryGivenRestaurantException;
import com.ti.acelera.plazoletamicroservice.domain.exceptions.RestaurantNotExistsException;
import com.ti.acelera.plazoletamicroservice.domain.exceptions.RoleNotAllowedException;
import com.ti.acelera.plazoletamicroservice.domain.gateway.IUserClient;
import com.ti.acelera.plazoletamicroservice.domain.model.Dish;
import com.ti.acelera.plazoletamicroservice.domain.model.Restaurant;
import com.ti.acelera.plazoletamicroservice.domain.spi.IDishPersistencePort;
import com.ti.acelera.plazoletamicroservice.domain.spi.IRestaurantPersistencePort;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserClient userClient;



    @Override
    public void saveDish(String userId,Dish dish) {
        Optional<Restaurant> restaurant = restaurantPersistencePort.getRestaurant(dish.getRestaurant().getId());

        if (restaurant.isEmpty()) {
            throw new RestaurantNotExistsException();
        }

        verifyOwner(userId, restaurant.get().getIdProprietary());

        dish.setActive(true);
        dishPersistencePort.saveDish(dish);
    }

    @Override
    public void modifyDish(String userId,Long dishId, Long price, String description) {
        Optional<Dish> dish = dishPersistencePort.getDish(dishId);

        if (dish.isEmpty()) {
            throw new DishNotFoundException();
        }
        verifyOwner(userId, dish.get().getRestaurant().getIdProprietary());

        if (price != null) {
            dish.get().setPrice( price );
        }
        if (description != null) {
            dish.get().setDescription( description );
        }

        dishPersistencePort.saveDish( dish.get() );
    }

    @Override
    public void modifyDishState( String proprietaryId, Long dishId, boolean dishState) {
        Optional<Dish> dish = dishPersistencePort.getDish( dishId );

        if(dish.isEmpty()){
            throw new DishNotFoundException();
        }

       verifyOwner( proprietaryId, dish.get().getRestaurant().getIdProprietary() );

       dish.get().setActive( dishState );
       dishPersistencePort.saveDish( dish.get() );

    }
    private void verifyOwner(String userId, String restaurantOwnerId) {
        final String userRole = userClient.getRoleByDni(userId);

        if (!userRole.equals("ROLE_OWNER")) {
            throw new RoleNotAllowedException();
        }

        if (!restaurantOwnerId.equals(userId)) {
            throw new NotProprietaryGivenRestaurantException();

        }
    }


}
