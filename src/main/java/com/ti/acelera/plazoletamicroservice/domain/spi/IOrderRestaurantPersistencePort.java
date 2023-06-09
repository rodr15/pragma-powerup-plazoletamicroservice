package com.ti.acelera.plazoletamicroservice.domain.spi;

import com.ti.acelera.plazoletamicroservice.domain.model.OrderRestaurant;
import com.ti.acelera.plazoletamicroservice.domain.model.OrderStatus;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IOrderRestaurantPersistencePort {

    Optional<List<OrderRestaurant>> getOrdersById(List<Long> ordersId);

    List<OrderRestaurant> saveAllOrderRestaurant(List<OrderRestaurant> ordersRestaurant);

    Page<OrderRestaurant> getOrdersList(Long restaurantId, OrderStatus state, int page, int size);
    List<OrderRestaurant> getOrdersList(Long restaurantId);
    List<OrderRestaurant> getOrdersListWithStatus(Long restaurantId,OrderStatus orderStatus);
    boolean clientHasUnfinishedOrders(Long clientId );
    OrderRestaurant createNewOrder(OrderRestaurant orderRestaurant);
    Optional<OrderRestaurant> getOrderById(Long orderId);
    OrderRestaurant saveOrderRestaurant(OrderRestaurant orderRestaurant);
    boolean restaurantHasUnfinishedOrders(Long restaurantId );
    void  deleteAllOrderRestaurant (List<OrderRestaurant> orderRestaurantList);
}
