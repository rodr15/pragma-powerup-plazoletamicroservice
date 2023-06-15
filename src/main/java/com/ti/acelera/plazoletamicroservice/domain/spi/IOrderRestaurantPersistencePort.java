package com.ti.acelera.plazoletamicroservice.domain.spi;

import com.ti.acelera.plazoletamicroservice.domain.model.OrderRestaurant;

public interface IOrderRestaurantPersistencePort {

    boolean hasUnfinishedOrders( Long clientId );
    OrderRestaurant createNewOrder(OrderRestaurant orderRestaurant);

}
