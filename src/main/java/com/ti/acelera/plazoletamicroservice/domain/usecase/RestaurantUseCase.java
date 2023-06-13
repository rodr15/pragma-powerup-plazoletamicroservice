package com.ti.acelera.plazoletamicroservice.domain.usecase;

import com.ti.acelera.plazoletamicroservice.domain.api.IRestaurantServicePort;
import com.ti.acelera.plazoletamicroservice.domain.exceptions.*;
import com.ti.acelera.plazoletamicroservice.domain.gateway.ISmsClient;
import com.ti.acelera.plazoletamicroservice.domain.gateway.IUserClient;
import com.ti.acelera.plazoletamicroservice.domain.model.*;
import com.ti.acelera.plazoletamicroservice.domain.spi.IDishOrderPersistencePort;
import com.ti.acelera.plazoletamicroservice.domain.spi.IDishPersistencePort;
import com.ti.acelera.plazoletamicroservice.domain.spi.IOrderRestaurantPersistencePort;
import com.ti.acelera.plazoletamicroservice.domain.spi.IRestaurantPersistencePort;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.*;

import static com.ti.acelera.plazoletamicroservice.configuration.Constants.SMS_READY_ORDER_MESSAGE;
import static java.lang.Long.parseLong;

@AllArgsConstructor
public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final IOrderRestaurantPersistencePort orderRestaurantPersistencePort;
    private final IDishOrderPersistencePort dishOrderPersistencePort;
    private final IUserClient userClient;
    private final ISmsClient smsClient;


    @Override
    public void assignEmployeeToOrder(String employeeId, List<Long> ordersId) {
        Long restaurantId = restaurantPersistencePort.getRestaurantIdByEmployeeId(parseLong(employeeId))
                .orElseThrow(EmployeeNotFindException::new);

        List<OrderRestaurant> restaurantOrdersList = orderRestaurantPersistencePort.getOrdersList(restaurantId);
        List<OrderRestaurant> selectedOrdersRestaurant = orderRestaurantPersistencePort.getOrdersById(ordersId)
                .orElseThrow(OrdersNotFoundException::new);

        boolean allIdsPresent = selectedOrdersRestaurant
                .stream()
                .map(OrderRestaurant::getId)
                .allMatch(restaurantOrder -> restaurantOrdersList.stream()
                        .anyMatch(selectedOrder -> selectedOrder.getId().equals(restaurantOrder)));

        if (!allIdsPresent) {
            throw new OrdersNotFoundException();
        }

        selectedOrdersRestaurant.forEach(selectedOrderRestaurant -> {
                    selectedOrderRestaurant.setIdChef(parseLong(employeeId));
                    selectedOrderRestaurant.setOrderStatus(selectedOrderRestaurant.getOrderStatus().next());
                }

        );
        orderRestaurantPersistencePort.saveAllOrderRestaurant(selectedOrdersRestaurant);
    }

    @Override
    public Page<OrderRestaurant> getOrdersPage(Long employeeId, OrderStatus state, int page, int size) {
        Long restaurantId = restaurantPersistencePort.getRestaurantIdByEmployeeId(employeeId)
                .orElseThrow(RestaurantNotExistsException::new);

        Page<OrderRestaurant> ordersList = orderRestaurantPersistencePort.getOrdersList(restaurantId, state, page, size);

        ordersList.forEach(orderRestaurant -> {
            List<DishOrder> dishOrders = dishOrderPersistencePort.getDishOrderByOrderRestaurantId(orderRestaurant.getId());
            if (dishOrders.isEmpty()) {
                throw new MalformedOrderException();
            }
            orderRestaurant.setDishes(dishOrders);
        });

        return ordersList;
    }

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

        if (dishIds.size() != uniqueDishIds.size()) {
            throw new MalformedOrderException();
        }

        List<Dish> dishesFromDatabase = dishPersistencePort.findAllDishesByIdAndByRestaurantId(orderRestaurant.getRestaurant().getId(), dishIds);

        if (dishesFromDatabase.size() != dishIds.size()) {
            throw new DishNotFoundException();
        }

        orderRestaurant.setOrderStatus(OrderStatus.EARRING_ORDER);
        orderRestaurant.setDate(LocalDate.now());


        return orderRestaurantPersistencePort.createNewOrder(orderRestaurant);
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
    public void assignEmployeeToRestaurant(String userId, Long restaurantId) {
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

    @Override
    public void orderRestaurantReady(Long orderRestaurantId) {
        OrderRestaurant orderRestaurant = orderRestaurantPersistencePort.getOrderById(orderRestaurantId).orElseThrow(OrdersNotFoundException::new);

        String pin = generateSecurityPin();

        orderRestaurant.setOrderStatus(OrderStatus.READY_ORDER);
        orderRestaurant.setVerificationCode(pin);

        String message = String.format(SMS_READY_ORDER_MESSAGE, pin);

        String clientPhone = userClient.getUserPhoneNumber(orderRestaurant.getIdClient().toString());
        String correctedClientPhone = correctPhoneNumber(clientPhone);
        smsClient.sendMessage( correctedClientPhone, message );

        orderRestaurantPersistencePort.saveOrderRestaurant( orderRestaurant );

    }


    private String correctPhoneNumber(String phoneNumber) {
        String digitsOnly = phoneNumber.replaceAll("\\D", "");
        if (digitsOnly.startsWith("3")) {
            digitsOnly = "+57" + digitsOnly;
        }

        if (!digitsOnly.startsWith("+")) {
            digitsOnly = "+" + digitsOnly;
        }
        if (digitsOnly.length() != 13) {
            throw new IllegalArgumentException("Invalid phone number format");
        }

        return digitsOnly;
    }
    private String generateSecurityPin() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

}
