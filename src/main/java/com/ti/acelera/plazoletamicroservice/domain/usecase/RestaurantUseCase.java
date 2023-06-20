package com.ti.acelera.plazoletamicroservice.domain.usecase;

import com.ti.acelera.plazoletamicroservice.domain.api.IRestaurantServicePort;
import com.ti.acelera.plazoletamicroservice.domain.exceptions.*;
import com.ti.acelera.plazoletamicroservice.domain.gateway.ISmsClient;
import com.ti.acelera.plazoletamicroservice.domain.gateway.ITraceabilityClient;
import com.ti.acelera.plazoletamicroservice.domain.gateway.IUserClient;
import com.ti.acelera.plazoletamicroservice.domain.model.*;
import com.ti.acelera.plazoletamicroservice.domain.spi.IDishOrderPersistencePort;
import com.ti.acelera.plazoletamicroservice.domain.spi.IDishPersistencePort;
import com.ti.acelera.plazoletamicroservice.domain.spi.IOrderRestaurantPersistencePort;
import com.ti.acelera.plazoletamicroservice.domain.spi.IRestaurantPersistencePort;
import com.ti.acelera.plazoletamicroservice.domain.utils.PhoneNumberUtils;
import com.ti.acelera.plazoletamicroservice.domain.utils.RandomVerificationCode;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
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
    private final ITraceabilityClient traceabilityClient;
    private final ISmsClient smsClient;


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

        if (orderRestaurant.getIdChef() == null) {
            throw new OrderNotAssignedException();
        }

        String pin = RandomVerificationCode.generateSecurityPin();

        orderRestaurant.setOrderStatus(OrderStatus.READY_ORDER);
        orderRestaurant.setVerificationCode(pin);

        String message = String.format(SMS_READY_ORDER_MESSAGE, pin);

        String clientPhone = userClient.getUserPhoneNumber(orderRestaurant.getIdClient().toString());
        String correctedClientPhone = PhoneNumberUtils.isValidColombianCellphoneNumber(clientPhone);


        smsClient.sendMessage(correctedClientPhone, message);
        traceabilityClient.modifyOrderTrace(orderRestaurant);

        orderRestaurantPersistencePort.saveOrderRestaurant(orderRestaurant);

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
        orderRestaurant.setDate(LocalDateTime.now());

        OrderRestaurant createdOrder = orderRestaurantPersistencePort.createNewOrder(orderRestaurant);
        traceabilityClient.saveOrderTrace(createdOrder);

        return createdOrder.getId();
    }

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
                    if (!selectedOrderRestaurant.getOrderStatus().equals(OrderStatus.EARRING_ORDER)) {
                        throw new OrderStatusNotAllowedForThisActionException();
                    }
                    selectedOrderRestaurant.setOrderStatus(selectedOrderRestaurant.getOrderStatus().next());
                    traceabilityClient.modifyOrderTrace(selectedOrderRestaurant);
                }

        );
        orderRestaurantPersistencePort.saveAllOrderRestaurant(selectedOrdersRestaurant);
    }

    @Override
    public void orderRestaurantDeliver(Long orderRestaurantId, String verificationCode, Long employeeId) {
        OrderRestaurant orderRestaurant = orderRestaurantPersistencePort
                .getOrderById(orderRestaurantId)
                .orElseThrow(OrdersNotFoundException::new);

        if (!Objects.equals(orderRestaurant.getIdChef(), employeeId)) {
            throw new OrderNotAssignedException();
        }

        if (!orderRestaurant.getOrderStatus().equals(OrderStatus.READY_ORDER)) {
            throw new NotAReadyOrderException();
        }

        if (!orderRestaurant.getVerificationCode().equals(verificationCode)) {
            throw new WrongVerificationCodeException();
        }

        orderRestaurant.setOrderStatus(orderRestaurant.getOrderStatus().next());
        traceabilityClient.modifyOrderTrace(orderRestaurant);

        orderRestaurantPersistencePort.saveOrderRestaurant(orderRestaurant);

    }

    @Override
    public void cancelOrder(Long userId, Long orderId) {
        OrderRestaurant orderRestaurant = orderRestaurantPersistencePort.getOrderById(orderId).orElseThrow();

        if (!orderRestaurant.getIdClient().equals(userId)) {
            throw new OrderNotFoundException();
        }

        if (!orderRestaurant.getOrderStatus().equals(OrderStatus.EARRING_ORDER)) {
            throw new OrderStatusNotAllowedForThisActionException();
        }

        orderRestaurant.setOrderStatus(OrderStatus.CANCELED_ORDER);
        orderRestaurantPersistencePort.saveOrderRestaurant(orderRestaurant);
        traceabilityClient.modifyOrderTrace(orderRestaurant);

    }

    @Override
    public List<Traceability> historyOrder(Long userId, Long orderId) {

        OrderRestaurant orderRestaurant = orderRestaurantPersistencePort.getOrderById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        if (!orderRestaurant.getIdClient().equals(userId)) {
            throw new OrderNotFoundException();
        }

        List<Traceability> traces = traceabilityClient.getOrderTrace(orderId);
        if (traces.isEmpty()) {
            throw new OrdersNotFoundException();
        }
        return traces;
    }


}
