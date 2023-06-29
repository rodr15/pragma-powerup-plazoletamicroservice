package com.ti.acelera.plazoletamicroservice.domain.transactions;

import com.ti.acelera.plazoletamicroservice.domain.exceptions.ThisClientHasUnfinishedOrdersException;
import com.ti.acelera.plazoletamicroservice.domain.gateway.ITraceabilityClient;
import com.ti.acelera.plazoletamicroservice.domain.model.*;
import com.ti.acelera.plazoletamicroservice.domain.spi.IDishOrderPersistencePort;
import com.ti.acelera.plazoletamicroservice.domain.spi.IDishPersistencePort;
import com.ti.acelera.plazoletamicroservice.domain.spi.IOrderRestaurantPersistencePort;
import com.ti.acelera.plazoletamicroservice.domain.spi.IRestaurantPersistencePort;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
public class DeleteRestaurantsTransaction {
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final IOrderRestaurantPersistencePort orderRestaurantPersistencePort;
    private final IDishOrderPersistencePort dishOrderPersistencePort;
    private final ITraceabilityClient traceabilityClient;
    @Transactional
    public void deleteRestaurant(Restaurant restaurant) {
        // Check if the restaurant has unfinished orders
        if (orderRestaurantPersistencePort.restaurantHasUnfinishedOrders(restaurant.getId())) {
            throw new ThisClientHasUnfinishedOrdersException();
        }

        // Delete OrderRestaurants and related DishOrders
        List<OrderRestaurant> orderRestaurantList = orderRestaurantPersistencePort.getOrdersList(restaurant.getId());
        for (OrderRestaurant orderRestaurant : orderRestaurantList) {
            List<DishOrder> dishOrderList = dishOrderPersistencePort.getDishOrderByOrderRestaurantId(orderRestaurant.getId());

            for (DishOrder dishOrder : dishOrderList) {
                // Save traceability for deleted DishOrders
                RestaurantObjectsTrace restaurantObjectsTrace = new RestaurantObjectsTrace(
                        restaurant.getId(),
                        "DELETED",
                        dishOrder.getId(),
                        "dishOrder"
                );
                traceabilityClient.saveRestaurantTrace(restaurantObjectsTrace);
            }

            dishOrderPersistencePort.deleteAllDishOrder(dishOrderList);

            // Save traceability for deleted OrderRestaurants
            RestaurantObjectsTrace restaurantObjectsTrace = new RestaurantObjectsTrace(
                    restaurant.getId(),
                    "DELETED",
                    orderRestaurant.getId(),
                    "orderRestaurant"
            );
            traceabilityClient.saveRestaurantTrace(restaurantObjectsTrace);
        }

        orderRestaurantPersistencePort.deleteAllOrderRestaurant(orderRestaurantList);

        // Delete dishes
        List<Dish> dishList = dishPersistencePort.findAllByRestaurantId(restaurant.getId());
        for (Dish dish : dishList) {
            // Save traceability for deleted dishes
            RestaurantObjectsTrace restaurantObjectsTrace = new RestaurantObjectsTrace(
                    restaurant.getId(),
                    "DELETED",
                    dish.getId(),
                    "dish"
            );
            traceabilityClient.saveRestaurantTrace(restaurantObjectsTrace);
        }

        dishPersistencePort.deleteAllDishes(dishList);

        // Delete the restaurant
        restaurant.setState(RestaurantState.DELETED);
        RestaurantObjectsTrace restaurantObjectsTrace = new RestaurantObjectsTrace(
                restaurant.getId(),
                "DELETED",
                restaurant.getId(),
                "restaurant"
        );
        traceabilityClient.saveRestaurantTrace(restaurantObjectsTrace);
        restaurantPersistencePort.deleteRestaurant(restaurant);
    }
}
