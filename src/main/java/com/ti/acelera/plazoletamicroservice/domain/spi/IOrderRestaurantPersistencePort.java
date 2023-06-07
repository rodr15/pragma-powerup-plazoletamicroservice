package com.ti.acelera.plazoletamicroservice.domain.spi;

import com.ti.acelera.plazoletamicroservice.domain.model.OrderRestaurant;
import org.springframework.data.domain.Page;

public interface IOrderRestaurantPersistencePort {

    Page<OrderRestaurant> getOrdersList(Long restaurantId, String state, int page, int size);
    boolean hasUnfinishedOrders( Long clientId );
    Long createNewOrder(OrderRestaurant orderRestaurant);

}
