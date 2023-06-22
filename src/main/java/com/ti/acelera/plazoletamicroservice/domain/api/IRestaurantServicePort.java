package com.ti.acelera.plazoletamicroservice.domain.api;

import com.ti.acelera.plazoletamicroservice.domain.model.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IRestaurantServicePort {
    void assignEmployeeToOrder(String employeeId, List<Long> ordersId);
    Page<OrderRestaurant> getOrdersPage(Long restaurantId, OrderStatus state, int page, int size);
    Long makeOrder(OrderRestaurant orderRestaurant) ;
    Page<Dish> pageDish( Long restaurantId, Long categoryId, int page,int size );
    Page<Restaurant> pageRestaurants(int page, int size);
    void saveRestaurant(Restaurant restaurant);
    boolean verifyRestaurantOwner(String userId, Long restaurantId);
    void assignEmployeeToRestaurant(String userId, Long restaurantId);
    void orderRestaurantReady(Long orderRestaurantId );
    void orderRestaurantDeliver(Long orderRestaurantId, String verificationCode, Long employeeId);
    void cancelOrder(Long userId, Long orderId);
    List<Traceability> historyOrder(Long userId, Long orderId);
    RestaurantStatistics restaurantStatistics(Long userId,Long restaurantId);
}
