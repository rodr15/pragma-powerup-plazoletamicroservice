package com.ti.acelera.plazoletamicroservice.domain.usecase;

import com.ti.acelera.plazoletamicroservice.domain.api.IRestaurantServicePort;
import com.ti.acelera.plazoletamicroservice.domain.exceptions.*;
import com.ti.acelera.plazoletamicroservice.domain.gateway.ITraceabilityClient;
import com.ti.acelera.plazoletamicroservice.domain.gateway.IUserClient;
import com.ti.acelera.plazoletamicroservice.domain.model.Dish;
import com.ti.acelera.plazoletamicroservice.domain.model.DishOrder;
import com.ti.acelera.plazoletamicroservice.domain.model.OrderRestaurant;
import com.ti.acelera.plazoletamicroservice.domain.model.Restaurant;
import com.ti.acelera.plazoletamicroservice.domain.spi.IDishPersistencePort;
import com.ti.acelera.plazoletamicroservice.domain.spi.IOrderRestaurantPersistencePort;
import com.ti.acelera.plazoletamicroservice.domain.spi.IRestaurantPersistencePort;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.ti.acelera.plazoletamicroservice.configuration.Constants.EARRING_ORDER;

@AllArgsConstructor
public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final IOrderRestaurantPersistencePort orderRestaurantPersistencePort;
    private final IUserClient userClient;
    private final ITraceabilityClient traceabilityClient;


    @Override
    public Long makeOrder(OrderRestaurant orderRestaurant) {

        if (orderRestaurantPersistencePort.hasUnfinishedOrders(orderRestaurant.getIdClient())) {
           throw new ThisClientHasUnfinishedOrdersException();
        }

        if (!restaurantPersistencePort.restaurantExists(orderRestaurant.getRestaurant().getId())) {
            throw new RestaurantNotExistsException();
        }


        List<Long> dishIds = orderRestaurant.getDishes()
                .stream()
                .map(DishOrder::getDish)
                .map(Dish::getId)
                .toList();

        Set<Long> uniqueDishIds = new HashSet<>(dishIds);

        if( dishIds.size() != uniqueDishIds.size() ){
                throw new MalformedOrderException();
        }

        List<Dish> dishesFromDatabase = dishPersistencePort.findAllDishesByIdAndByRestaurantId(orderRestaurant.getRestaurant().getId(), dishIds);

        if (dishesFromDatabase.size() != dishIds.size()) {
            throw new DishNotFoundException();
        }

        orderRestaurant.setState(EARRING_ORDER);
        orderRestaurant.setDate(LocalDate.now());

         OrderRestaurant createdOrder =  orderRestaurantPersistencePort.createNewOrder(orderRestaurant);

        traceabilityClient.saveOrderTrace(createdOrder);

        return createdOrder.getId();
    }

    @Override
    public Page<Dish> pageDish(Long restaurantId, Long categoryId, int page, int size) {

        if (page < 0 || size <= 0) {
            throw new BadPagedException();
        }

        Optional<Restaurant> restaurant = restaurantPersistencePort.getRestaurant(restaurantId);
        if (restaurant.isEmpty()) {
            throw new RestaurantNotExistsException();
        }

        Page<Dish> dishPage;

        if (categoryId == null) {
            dishPage = dishPersistencePort.getActiveDishesByRestaurantId(restaurantId, page, size);
        } else {
            dishPage = dishPersistencePort.getActiveDishesByRestaurantId(restaurantId, categoryId, page, size);
        }


        return dishPage;
    }

    @Override
    public Page<Restaurant> pageRestaurants(int page, int size) {

        if (page < 0 || size <= 0) {
            throw new BadPagedException();
        }

        return restaurantPersistencePort.getAllRestaurants(page, size);
    }

    @Override
    public void saveRestaurant(Restaurant restaurant) {
        final String role = userClient.getRoleByDni(restaurant.getIdProprietary());

        if (!role.equals("ROLE_OWNER")) {
            throw new RoleNotAllowedException();
        }

        restaurantPersistencePort.saveRestaurant(restaurant);
    }

    @Override
    public boolean verifyRestaurantOwner(String userId, Long restaurantId) {

        Optional<Restaurant> restaurant = restaurantPersistencePort.getRestaurant(restaurantId);

        if (restaurant.isEmpty()) {
            throw new RestaurantNotExistsException();
        }

        return restaurant.get().getIdProprietary().equals(userId);
    }

    @Override
    public void assignEmployee(String userId, Long restaurantId) {
        Optional<Restaurant> restaurant = restaurantPersistencePort.getRestaurant(restaurantId);

        if (restaurant.isEmpty()) {
            throw new RestaurantNotExistsException();
        }

        Set<String> employees = restaurant.get().getEmployees();

        if (employees == null || employees.isEmpty()) {
            employees = new HashSet<>();
        }

        employees.add(userId);
        restaurant.get().setEmployees(employees);
        restaurantPersistencePort.saveRestaurant(restaurant.get());
    }
}
