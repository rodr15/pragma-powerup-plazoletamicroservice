package com.ti.acelera.plazoletamicroservice.domain.spi;

import com.ti.acelera.plazoletamicroservice.domain.model.OrderRestaurant;
import com.ti.acelera.plazoletamicroservice.domain.model.OrderStatus;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IOrderRestaurantPersistencePort {

    Optional<List<OrderRestaurant>> getOrdersById(List<Long> ordersId);
    List<OrderRestaurant> saveAllOrderRestaurant(List<OrderRestaurant> ordersRestaurant );
    Page<OrderRestaurant> getOrdersList(Long restaurantId, OrderStatus state, int page, int size);
    List<OrderRestaurant> getOrdersList(Long restaurantId);
    boolean hasUnfinishedOrders( Long clientId );
    OrderRestaurant createNewOrder(OrderRestaurant orderRestaurant);

}
