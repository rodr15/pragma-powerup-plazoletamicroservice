package com.ti.acelera.plazoletamicroservice.domain.usecase;

import com.ti.acelera.plazoletamicroservice.domain.api.IRestaurantServicePort;
import com.ti.acelera.plazoletamicroservice.domain.exceptions.BadPagedException;
import com.ti.acelera.plazoletamicroservice.domain.exceptions.RestaurantNotExistsException;
import com.ti.acelera.plazoletamicroservice.domain.exceptions.RoleNotAllowedException;
import com.ti.acelera.plazoletamicroservice.domain.gateway.IUserClient;
import com.ti.acelera.plazoletamicroservice.domain.model.Restaurant;
import com.ti.acelera.plazoletamicroservice.domain.spi.IRestaurantPersistencePort;
import org.springframework.data.domain.Page;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserClient userClient;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort, IUserClient userClient) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userClient = userClient;
    }

    @Override
    public Page<Restaurant> pageRestaurants(int page, int size) {

        if( page < 0 || size <= 0){
            throw new BadPagedException();
        }

        return restaurantPersistencePort.getAllRestaurants( page, size  );
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

        if(restaurant.isEmpty()) {
            throw  new RestaurantNotExistsException();
        }

        return restaurant.get().getIdProprietary().equals(userId);
    }

    @Override
    public void assignEmployee(String userId, Long restaurantId) {
        Optional<Restaurant> restaurant = restaurantPersistencePort.getRestaurant( restaurantId );

        if( restaurant.isEmpty() ) {
            throw new RestaurantNotExistsException();
        }

        Set<String> employees = restaurant.get().getEmployees();

        if (employees == null ||  employees.isEmpty()) {
            employees = new HashSet<>();
        }

        employees.add(userId);
        restaurant.get().setEmployees(employees);
        restaurantPersistencePort.saveRestaurant(restaurant.get());
    }
}
