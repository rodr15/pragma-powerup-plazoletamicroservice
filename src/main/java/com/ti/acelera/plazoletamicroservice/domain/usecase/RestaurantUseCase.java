package com.ti.acelera.plazoletamicroservice.domain.usecase;

import com.ti.acelera.plazoletamicroservice.domain.api.IRestaurantServicePort;
import com.ti.acelera.plazoletamicroservice.domain.exceptions.BadPagedException;
import com.ti.acelera.plazoletamicroservice.domain.exceptions.RestaurantNotExistsException;
import com.ti.acelera.plazoletamicroservice.domain.exceptions.RoleNotAllowedException;
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


    @Override
    public OrderRestaurant makeOrder(OrderRestaurant orderRestaurant) {

        if (orderRestaurantPersistencePort.hasUnfinishedOrders(orderRestaurant.getIdClient())) {
            //TODO: CHANGE FOR EXCEPTIONS
            System.out.println(" EL CLIENTE TIENE PEDIDOS SIN FINALIZAR ");
            return null;
        }

        if (!restaurantPersistencePort.restaurantExists(orderRestaurant.getRestaurant().getId())) {
            //TODO: CHANGE FOR EXCEPTIONS
            System.out.println(" EL RESTAURANTE ELEGIDO NO EXISTE ");
            return null;
        }


        List<Long> dishIds = orderRestaurant.getDishes()
                .stream()
                .map(DishOrder::getDish)
                .map(Dish::getId)
                .toList();

        List<Dish> dishesFromDatabase = dishPersistencePort.findAllDishesByIdAndByRestaurantId(orderRestaurant.getRestaurant().getId(), dishIds);

        if (dishesFromDatabase.size() != dishIds.size()) {
            //TODO: CHANGE FOR EXCEPTIONS
            System.out.println(" NO SE HA ENCONTRADO ALGUN PLATO ");
            return null;
        }

        orderRestaurant.setState(EARRING_ORDER);
        orderRestaurant.setDate(LocalDate.now());

        orderRestaurantPersistencePort.createNewOrder(orderRestaurant);
        return orderRestaurant;
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
