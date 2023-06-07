package com.ti.acelera.plazoletamicroservice.domain.spi;

import com.ti.acelera.plazoletamicroservice.domain.model.DishOrder;

import java.util.List;

public interface IDishOrderPersistencePort {
    List<DishOrder> getDishOrderByOrderRestaurantId(Long restaurantId );
}
